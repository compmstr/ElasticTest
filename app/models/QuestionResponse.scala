package models

import org.joda.time.DateTime

case class QuestionResponse(id: Long, user: Long, question: Long, text: String, created: DateTime, tags: Array[String] = Array())

object QuestionResponse{
  private var responses = Map[Long, QuestionResponse]()
  private var nextId: Long = 1;

  def addResponse(q: QuestionResponse) = {
	  QuestionResponse.this.synchronized {
		  responses = responses + ((nextId, q.copy(id = nextId)))
		  nextId = nextId + 1
	  }
  }
  
  def getById: Long => Option[QuestionResponse] = responses.get _
  def getAll: List[QuestionResponse] = responses.values.toList
  def getByQuestion(question: Long): List[QuestionResponse] = responses.values.filter(_.question == question).toList
  def getByUser(user: Long): List[QuestionResponse] = responses.values.filter(_.user == user).toList

  addResponse(QuestionResponse(-1, 2, 1, "Example Response", DateTime.now()))
  addResponse(QuestionResponse(-1, 3, 2, "Example Response 2", DateTime.now()))
  addResponse(QuestionResponse(-1, 4, 3, "Example Response 3", DateTime.now()))
  addResponse(QuestionResponse(-1, 2, 4, "Another Response", DateTime.now()))
  addResponse(QuestionResponse(-1, 5, 5, "It works this way", DateTime.now()))
}