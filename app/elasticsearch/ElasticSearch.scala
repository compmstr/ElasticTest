package elasticsearch

import play.api.libs.json._
import play.api.libs.ws._
import models.Question
import models.QuestionResponse
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import org.joda.time.DateTime

trait Indexable {
  val index: String
  val dataType: String
  val id: String
  val indexData: JsValue
  
  def toBatchEntry(action: String = "index"): String = {
    val actionLine = Json.obj(action -> 
	    Json.obj("_index" -> index,
	    	"_type" -> dataType,
	    	"_id" -> id)
	    ).toString
    s"$actionLine\n${indexData.toString}\n"
  }
  
  val mapping: JsValue
}

trait Index {
  val index: String
  def create: Future[Boolean]
  def delete: Future[Boolean] = {
    ElasticSearch.deleteIndex(index).map(r => (r.status == 200 || r.status == 404))
  }
  def query(q: String): Future[List[Indexable]]
}

object ElasticSearch {
	  
	val elasticAPIUrl = "http://localhost:9200/"
	  
	def createIndex(i: String, data: JsValue): Future[Response] = {
	  WS.url(s"$elasticAPIUrl/$i")
	    .withHeaders("Content-Type" -> "application/json")
	    .post(data)
	    .map(printResponse)
	}
	  
	def deleteIndex(i: String): Future[Response] = {
	  WS.url(s"$elasticAPIUrl/$i").delete
      .map(printResponse)
	}
	
	def indexBatch(is: List[Indexable]): Future[Response] = {
	  println("Indexing list of objects")
	  WS.url(s"$elasticAPIUrl/_bulk")
	  	.withHeaders("Content-Type" -> "application/json")
	  	.post(is.map(_.toBatchEntry("index")).mkString(""))
	  	.map(printResponse)
	}
	  
	def indexDocument(i: Indexable): Future[Response] = {
	  println("Sending data: " + i.indexData.toString)
	  WS.url(s"$elasticAPIUrl/${i.index}/${i.dataType}/${i.id}")
	  	.withHeaders("Content-Type" -> "application/json")
	  	.put(i.indexData)
        .map(printResponse)
	}
	
	def printResponse(r: Response): Response = {
	  println("Got Response: " + r.status + " -- " + r.statusText + "\n" + r.body)
	  r
	}
}
