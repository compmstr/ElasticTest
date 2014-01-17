package models

import org.joda.time.DateTime

case class Question(id: Long, user: Long, course: Long, lecture: Long, text: String, created: DateTime)

object Question{
  private var questions = Map[Long, Question]()
  private var nextId: Long = 1;

  def addQuestion(q: Question) = {
	  this.synchronized {
		  questions = questions + ((nextId, q.copy(id = nextId)))
		  nextId = nextId + 1
	  }
  }
  
  def getById: Long => Option[Question] = questions.get _
  def getAll: List[Question] = questions.values.toList
  def getByCourse(course: Long): List[Question] = questions.values.filter(_.course == course).toList
  def getByUser(user: Long): List[Question] = questions.values.filter(_.user == user).toList
  def getByLecture(lecture: Long): List[Question] = questions.values.filter(_.lecture == lecture).toList

  addQuestion(Question(-1, 2, 3, 1, "Example Question", DateTime.now()))
  addQuestion(Question(-1, 3, 3, 1, "Example Question 2", DateTime.now()))
  addQuestion(Question(-1, 4, 3, 2, "Example Question 3", DateTime.now()))
  addQuestion(Question(-1, 2, 2, 1, "Another Question", DateTime.now()))
  addQuestion(Question(-1, 5, 2, 2, "How does this work?", DateTime.now()))
}