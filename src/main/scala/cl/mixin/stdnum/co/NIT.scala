package cl.mixin.stdnum.co

import cl.mixin.stdnum.{
  Identity,
  InvalidChecksum,
  InvalidFormat,
  InvalidLength,
  Tools,
  ValidationError
}

/** NIT (Número De Identificación Tributaria, Colombian identity code).
  *
  * This number, also referred to as RUT (Registro Unico Tributario) is the Colombian business tax
  * number.
  */
object NIT extends Identity {
  override def compact(number: String): String = Tools.clean(number, Vector('.', ',', '-', ' '))

  override def format(number: String, separator: String): String =
    val compactNumber = this.compact(number)
    val compactNumberLength = compactNumber.length
    Range(-1, -compactNumber.length, -3).reverse
      .map(index =>
        compactNumber.slice(index - 3 + compactNumberLength, compactNumberLength + index)
      )
      .mkString(".") + "-" + compactNumber.last

  override def validate(
    number: String,
    validateCheckDigit: Boolean
  ): Either[ValidationError, String] =
    val compactNumber = this.compact(number)
    if !(8 <= compactNumber.length && compactNumber.length <= 16) then Left(InvalidLength())
    else if !Tools.isDigits(compactNumber) then Left(InvalidFormat())
    else if validateCheckDigit && this.calcCheckDigits(compactNumber.init) != compactNumber.last then
      Left(InvalidChecksum())
    else Right(compactNumber)

  private def calcCheckDigits(number: String): Char =
    val weights = Vector(3, 7, 13, 17, 19, 23, 29, 37, 41, 43, 47, 53, 59, 67, 71)
    val sum = weights.zip(number.reverse).map((w, n) => w * n.asDigit).sum
    "01987654321" (math.floorMod(sum, 11))

}
