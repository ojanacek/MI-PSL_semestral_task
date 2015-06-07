package data

object Learnt extends Enumeration {
  val Yes, Partially, No = Value

  def parseString(text: String) =
    text match {
      case "Yes" => Learnt.Yes
      case "Partially" => Learnt.Partially
      case "No" => Learnt.No
    }
}
