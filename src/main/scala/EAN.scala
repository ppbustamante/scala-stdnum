package stdnum

/** EAN (International Article Number).
  *
  * Module for handling EAN (International Article Number) codes. This module handles numbers
  * EAN-13, EAN-8, UPC (12-digit) and GTIN (EAN-14) format.
  */
object EAN extends Validator {

  override def compact(number: String): String =
    Tools.clean(number, Vector(' ', '-'))

  override def format(number: String, separator: String): String = number

  override def validate(
    number: String,
    validateCheckDigit: Boolean
  ): Either[ValidationError, String] = {
    val compactNumber = this.compact(number)
    if !Tools.isDigits(compactNumber) then Left(InvalidFormat())
    else if !Vector(14, 13, 12, 8).contains(compactNumber.length) then Left(InvalidLength())
    else if this.calcCheckDigit(compactNumber.init) != compactNumber.last then
      Left(InvalidChecksum())
    else Right(compactNumber)
  }

  private def calcCheckDigit(number: String): Char = {
    val sum = number.reverse.zipWithIndex.map { case (n, i) =>
      (if (i % 2 == 0) 3 else 1) * n.asDigit
    }.sum
    math.floorMod(10 - sum, 10).toString.charAt(0)
  }

}
