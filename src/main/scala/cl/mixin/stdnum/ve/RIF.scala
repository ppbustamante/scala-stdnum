package cl.mixin.stdnum.ve

import cl.mixin.stdnum.{
  Identity,
  InvalidChecksum,
  InvalidComponent,
  InvalidFormat,
  InvalidLength,
  Tools,
  ValidationError
}

/** RIF (Registro de Identificación Fiscal, Venezuelan VAT number).
  *
  * The Registro de Identificación Fiscal (RIF) is the Venezuelan fiscal registration number. The
  * number consists of 10 digits where the first digit denotes the type of number (person, company
  * or government) and the last digit is a check digit.
  */
object RIF extends Identity {
  private val COMPANY_TYPE = Map(
    'V' -> 4,
    'E' -> 8,
    'J' -> 12,
    'P' -> 16,
    'G' -> 20
  )
  override def compact(number: String): String = Tools.clean(number, Vector(' ', '-'))

  override def format(number: String, separator: String = "-"): String =
    val compactNumber = this.compact(number)
    s"${compactNumber.head}$separator${compactNumber.drop(1).init}$separator${compactNumber.last}"

  override def validate(
    number: String,
    validateCheckDigit: Boolean
  ): Either[ValidationError, String] =
    val compactNumber = this.compact(number)
    if compactNumber.length != 10 then Left(InvalidLength())
    else if !COMPANY_TYPE.contains(compactNumber.head) then Left(InvalidComponent())
    else if !Tools.isDigits(compactNumber.tail) then Left(InvalidFormat())
    else if validateCheckDigit && (this
          .calcCheckDigits(compactNumber)
          .isEmpty || this.calcCheckDigits(compactNumber).get != compactNumber.last)
    then Left(InvalidChecksum())
    else Right(compactNumber)

  private def calcCheckDigits(number: String): Option[Char] =
    val compactNumber = this.compact(number)
    val weights = Vector(3, 2, 7, 6, 5, 4, 3, 2)
    COMPANY_TYPE
      .get(compactNumber(0))
      .map(company =>
        company + weights.zip(compactNumber.slice(1, 9)).map((w, n) => w * n.asDigit).sum
      )
      .map(sum => "00987654321" (math.floorMod(sum, 11)))
}
