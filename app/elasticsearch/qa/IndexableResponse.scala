package elasticsearch.qa

import models.QuestionResponse
import play.api.libs.json._
import scala.language.implicitConversions

object IndexableResponse {
  implicit def responseConvert(r: QuestionResponse): IndexableResponse = new IndexableResponse(r)
  val mapping = Json.obj(
      "response" ->
      	Json.obj(
      	    "properties" ->
      	    	Json.obj(
      	    	    "id" -> Json.obj(
		      	    	    		"type" -> "long"
		      	    	    		),
      	    	    "user" -> Json.obj(
		      	    	    		"type" -> "long"
		      	    	    		),
      	    	    "question" -> Json.obj(
		      	    	    		"type" -> "long"
		      	    	    		),
      	    	    "text" -> Json.obj(
		      	    	    		"type" -> "string",
		      	    	    		"analyzer" -> "snowball"
		      	    	    		),
      	    	    "created" -> Json.obj(
		      	    	    		"type" -> "long"
		      	    	    		)
      	    	    )
      	    )
      )
}
class IndexableResponse(r: QuestionResponse) extends QAIndex.QAIndexable {
  private def responseId(r: QuestionResponse): String = "response_" + r.id
  val id = responseId(r)
  val dataType = "response"

  private case class ResponseData(id: Long, user: Long, question: Long, text: String, created: Long)
  private implicit def responseDataConvert(r: QuestionResponse): ResponseData = 
    ResponseData(r.id, r.user, r.question, r.text, r.created.getMillis())
  private implicit val responseDataWriter = Json.writes[ResponseData]

  lazy val indexData = Json.toJson[ResponseData](r)
  
  val mapping = IndexableResponse.mapping
}
