package cl.mixin.stdnum

import scala.Vector

/** ISBN (International Standard Book Number).
  *
  * The ISBN is the International Standard Book Number, used to identify publications. An ISBN is
  * used to identify books. Numbers can either have 10 digits (in ISBN-10 format) or 13 digits (in
  * ISBN-13, EAN compatible format). An ISBN has the following components:
  *
  * * 3-digit (only in ISBN-13) Bookland code * 1 to 5-digit group identifier (identifies country or
  * language) * 1 to 7-digit publisher code * 1 to 8-digit item number (identifies the book) * a
  * check digit
  *
  * More information:
  *
  * * https://en.wikipedia.org/wiki/International_Standard_Book_Number *
  * https://www.isbn-international.org/range_file_generation
  */
object ISBN extends Validator {

  override def compact(number: String): String =
    val cleanedNumber = Tools.clean(number, Vector(' ', '-'))
    if (cleanedNumber.length == 9)
      return "0" + cleanedNumber
    cleanedNumber

  override def format(number: String, separator: String): String =
    number

  override def validate(
    number: String,
    validateCheckDigit: Boolean
  ): Either[ValidationError, String] = {
    val compactNumber = this.compact(number)
    if !Tools.isDigits(compactNumber.init) then Left(InvalidFormat())
    else if compactNumber.length != 10 && compactNumber.length != 13 then Left(InvalidLength())
    else if compactNumber.length == 10 && this.calcISBN10CheckDigit(
          compactNumber.init
        ) != compactNumber.last
    then Left(InvalidChecksum())
    else if compactNumber.length == 13 && EAN.validate(compactNumber).isLeft then
      println(1)
      Left(InvalidComponent())
    else if compactNumber.length == 13 && !Vector("978", "979").contains(compactNumber.take(3)) then
      println(2)
      Left(InvalidComponent())
    else Right(compactNumber)
  }

  private def isbnType(number: String): Option[String] =
    this.validate(number) match
      case Left(value) => None
      case Right(value) => if (value.length == 10) Option("ISBN10") else Option("ISBN13")

  private def calcISBN10CheckDigit(number: String): Char = {
    val check = number.zipWithIndex.map { case (n, i) => (i + 1) * n.toString.toInt }.sum % 11
    if (check == 10) 'X' else check.toChar
  }
}
