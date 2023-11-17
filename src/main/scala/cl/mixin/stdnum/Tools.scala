package cl.mixin.stdnum

object Tools:
  /** Check whether the provided string only consists of digits.
    */
  def isDigits(number: String): Boolean = number.forall(_.isDigit)

  /** Remove the specified characters from the supplied number.
    */
  def clean(
    number: String,
    deleteChars: Vector[Char] = Vector.empty[Char]
  ): String =
    // TODO: Replace various Unicode characters with their ASCII counterpart.
    (for {
      char <- number.toVector
      if !deleteChars.contains(char)
    } yield char).mkString
