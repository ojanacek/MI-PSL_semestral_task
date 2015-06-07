package data

import scala.collection.mutable.ListBuffer

case class QuestionBook(categories: List[Category]) {
  def withoutLearnt(): QuestionBook = {
    val newCategories = List[Category]()
    for (cat <- categories) {
      val needToLearnQuestions = cat.questions.filter(_.learnt != Learnt.Yes)
      if (needToLearnQuestions.nonEmpty)
        cat.copy(questions = needToLearnQuestions.toList) :: newCategories
    }
    new QuestionBook(newCategories)
  }
}

object QuestionBook {

  def parseFromText(text: Iterator[String]) : QuestionBook = {
    val categories = new ListBuffer[Category]()
    val questions = new ListBuffer[Question]()

    for (line <- text) {
      val lineSymbol = line.take(2)

      lineSymbol match {
        case "> " => {
          if (questions.nonEmpty) {
            val lastCategory = categories.last
            categories.update(categories.size - 1, lastCategory.copy(questions = questions.toList))
            questions.clear()
          }

          categories += new Category(line.substring(2), categories.size + 1, null)
        }
        case ">>" =>
          val question = new Question(line.substring(3), questions.size + 1)
          questions += question
        case "$~" =>
          val parts = line.substring(2).split("~")
          val learnt = Learnt.parseString(parts(0))
          val notes = if (parts.size == 2) parts(1) else ""
          val lastQuestion = questions.last
          questions.update(questions.size - 1, lastQuestion.copy(learnt = learnt, notes = notes))
        case _ => { } // machtes empty lines at the end
      }
    }

    if (questions.nonEmpty) {
      val lastCategory = categories.last
      categories.update(categories.size - 1, lastCategory.copy(questions = questions.toList))
      questions.clear()
    }

    new QuestionBook(categories.toList)
  }
}
