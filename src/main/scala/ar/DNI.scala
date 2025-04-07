package stdnum.ar

import stdnum.{InvalidFormat, InvalidLength, Tools, ValidationError, Validator}

/** DNI (Documento Nacional de Identidad, Argentinian national identity nr.). */
object DNI extends Validator {

  override def compact(number: String): String =
    Tools.clean(number, Vector(' ', '-', '.'))

  override def validate(
    number: String,
    validateCheckDigit: Boolean = true
  ): Either[ValidationError, String] =
    val compactNumber = this.compact(number)
    if !Tools.isDigits(compactNumber) then Left(InvalidFormat())
    else if compactNumber.length != 7 && compactNumber.length != 8 then Left(InvalidLength())
    else Right(compactNumber)

  override def format(number: String, separator: String = "."): String =
    val fNumber = this.compact(number)
    s"${fNumber.dropRight(6)}$separator${fNumber.dropRight(3).takeRight(3)}$separator${fNumber.takeRight(3)}"
}
