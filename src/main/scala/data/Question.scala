package data

case class Question(text: String, number: Int, var learnt: Learnt.Value = Learnt.No, var notes: String = "") {
  override def toString = s"$number - $text"
}
