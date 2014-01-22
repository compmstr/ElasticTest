package elasticsearch.qa

import models.Question
import play.api.libs.json._
import scala.language.implicitConversions

object IndexableQuestion {
  implicit def questionConvert(q: Question): IndexableQuestion = new IndexableQuestion(q)

  val mapping = Json.obj(
      "question" ->
      	Json.obj(
      	    "properties" ->
      	    	Json.obj(
      	    	    "id" -> Json.obj(
		      	    	    		"type" -> "long"
		      	    	    		),
      	    	    "user" -> Json.obj(
		      	    	    		"type" -> "long"
		      	    	    		),
      	    	    "course" -> Json.obj(
		      	    	    		"type" -> "long"
		      	    	    		),
      	    	    "tags" -> Json.obj(
		      	    	    		"type" -> "string",
		      	    	    		"index_name" -> "tags"
		      	    	    		),
      	    	    "lecture" -> Json.obj(
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
class IndexableQuestion(q: Question) extends QAIndex.QAIndexable {
  private def questionId(q: Question): String = "question_" + q.id
  val id = questionId(q)
  val dataType = "question"

  private case class QuestionData(id: Long, user: Long, course: Long, lecture: Long, text: String, created: Long, tags: Array[String])
  private implicit def questionDataConvert(q: Question): QuestionData = 
    QuestionData(q.id, q.user, q.course, q.lecture, q.text, q.created.getMillis(), q.tags)
  private implicit val questionDataWriter = Json.writes[QuestionData]

  lazy val indexData = Json.toJson[QuestionData](q)
  
  val mapping = IndexableQuestion.mapping
}