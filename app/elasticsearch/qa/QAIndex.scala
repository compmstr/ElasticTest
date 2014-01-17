package elasticsearch.qa

import elasticsearch._
import play.api.libs.json._
import scala.concurrent.ExecutionContext.Implicits.global

object QAIndex extends Index {
  val index = "qa"
  trait QAIndexable extends Indexable {
    val index = QAIndex.index
  }
  
  def create = {
    ElasticSearch.createIndex(index, 
        Json.obj("settings" -> Json.obj(),
            "mappings" -> (IndexableQuestion.mapping ++ IndexableResponse.mapping)))
        .map(r => r.status == 200)
  }
  def query(q: String) = ???
}
