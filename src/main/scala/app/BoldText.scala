package app

import scalafx.scene.paint.Color
import scalafx.scene.text.Text

class BoldText(text: String) extends Text(text) {
  style = "-fx-font-weight: bold"

  def this(text: String, color: Color) {
    this(text)
    fill = color
  }
}
