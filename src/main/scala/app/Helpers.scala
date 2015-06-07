package app

import data._

import javafx.scene.control.{Tab}

import scala.io.Source
import scalafx.geometry.Insets
import scalafx.scene.Node
import scalafx.scene.control.{TabPane}
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color
import scalafx.scene.text.{Text, TextFlow}

object Helpers {

  def loadFile(path: String) =
    Source.fromFile(path).getLines()

  def createQuestionPanel(book: QuestionBook) : Node = {
    val panel = new TabPane() { margin = Insets(30, 0, 0, 0) }
    Helpers.createTabsForCategories(panel, book.categories)
    return panel
  }

  private def createTabsForCategories(panel: TabPane, categories: List[Category]) {
    categories match {
      case Nil => ()
      case list => {
        val category = list.head
        val box = new VBox()
        createQuestionNodes(box, category.questions)
        panel.tabs.add(new Tab(category.title, box))
        createTabsForCategories(panel, list.tail)
      }
    }
  }

  private def createQuestionNodes(box: VBox, questions: List[Question]) {
    questions match {
      case Nil => ()
      case list => {
        val question = list.head
        val questionText = new Text(question.toString) {
          fill = matchColorWithLearnt(question.learnt)
          style = "-fx-font-size: 17px;"
          margin = Insets(5, 0, 0, 5)
        }
        box.children.add(questionText)

        if (question.notes.length > 0) {
          val notesText = new Text(question.notes) {
            fill = Color.Black
            style = "-fx-font-size: 15px;"
            margin = Insets(0, 0, 0, 30)
          }
          box.children.add(notesText)
        }
        createQuestionNodes(box, list.tail)
      }
    }
  }

  private def matchColorWithLearnt(learnt: Learnt.Value) =
    learnt match {
      case Learnt.Yes => Color.Green
      case Learnt.Partially => Color.Orange
      case Learnt.No => Color.Red
    }

  def printBookInfo(book: QuestionBook) : Node = {
    val questionsCount = new BoldText(book.categories.flatMap(_.questions).length.toString)
    val categoriesCount = new BoldText(book.categories.length.toString)

    new TextFlow {
      children = Seq(
        new Text("There's "),
        questionsCount,
        new Text(" questions in "),
        categoriesCount,
        new Text(" categories.")
      )
    }
  }

  def printQuestionsSummary(book: QuestionBook) : Node = {
    val questions = book.categories.flatMap(_.questions)

    val learntText = new BoldText("Learnt: ", Color.Green)
    val learntCountText = new BoldText(questions.count(_.learnt == Learnt.Yes).toString)

    val partLearntText = new BoldText("Partially learnt: ", Color.Orange)
    val partLearntCountText = new BoldText(questions.count(_.learnt == Learnt.Partially).toString)

    val notLearntText = new BoldText("Not learnt: ", Color.Red)
    val notLearntCount = questions.count(_.learnt == Learnt.No)
    val notLearntPercentage = notLearntCount / questions.length.toDouble
    val notLearntCountText = new BoldText(f"$notLearntCount%s ($notLearntPercentage%2.2f%%)")

    new VBox() {
      children = Seq(
        new TextFlow(learntText, learntCountText),
        new TextFlow(partLearntText, partLearntCountText),
        new TextFlow(notLearntText, notLearntCountText)
      )
    }
  }
}
