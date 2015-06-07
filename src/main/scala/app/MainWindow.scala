package app

import javafx.event.ActionEvent
import javafx.event.EventHandler

import data._

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.{Insets}
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout.{VBox}
import scalafx.scene.paint.Color._

object MainWindow extends JFXApp {

  val textLines = Helpers.loadFile("C:/test/questions.txt")
  val book = QuestionBook.parseFromText(textLines)
  var panel = Helpers.createQuestionPanel(book)
  val hideLearntCheck = new CheckBox {
    text = "Hide learnt questions"
    margin = Insets(10, 0, 0, 0)
  }
  hideLearntCheck.onAction = new EventHandler[ActionEvent] {
    def handle(event: ActionEvent) {
      if (hideLearntCheck.selected == true)
        panel = Helpers.createQuestionPanel(book.withoutLearnt)
      else
        panel = Helpers.createQuestionPanel(book)

      stage = drawStage()
    }
  }

  stage = drawStage()

  private def drawStage(): PrimaryStage = {
    new PrimaryStage {
      title = "MI-PSL exam questions"
      scene = new Scene {
        fill = White
        content = new VBox {
          padding = Insets(20)
          children = Seq(
            Helpers.printBookInfo(book),
            Helpers.printQuestionsSummary(book),
            Helpers.createQuestionPanel(book),
            hideLearntCheck
          )
        }
      }
    }
  }
}
