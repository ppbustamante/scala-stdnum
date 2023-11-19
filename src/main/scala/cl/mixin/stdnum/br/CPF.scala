package cl.mixin.stdnum.br

import cl.mixin.stdnum.{
  Identity,
  InvalidChecksum,
  InvalidFormat,
  InvalidLength,
  Tools,
  ValidationError
}

/** CPF (Cadastro de Pessoas Físicas, Brazilian national identifier).
  *
  * The Cadastro de Pessoas Físicas is the Brazilian identification number assigned to individuals
  * for tax purposes. The number consists of 11 digits and includes two check digits.
  *
  * More information:
  *
  *   - https://en.wikipedia.org/wiki/Cadastro_de_Pessoas_Físicas
  */
object CPF extends Identity {
  override def compact(number: String): String =
    Tools.clean(number, Vector(' ', '-', '.'))

  override def format(number: String, separator: String = "."): String =
    val compactNumber = this.compact(number)
    s"${compactNumber.take(3)}$separator${compactNumber
        .slice(3, 6)}$separator${compactNumber.drop(6).dropRight(2)}-${compactNumber.takeRight(2)}"

  override def validate(
    number: String,
    validateCheckDigit: Boolean
  ): Either[ValidationError, String] =
    val compactNumber = this.compact(number)
    if !Tools.isDigits(compactNumber) || compactNumber.toLong <= 0 then Left(InvalidFormat())
    else if compactNumber.length != 11 then Left(InvalidLength())
    else if validateCheckDigit && this.calcCheckDigits(compactNumber) != compactNumber.takeRight(2)
    then Left(InvalidChecksum())
    else Right(compactNumber)

  private def calcCheckDigits(number: String): String =
    var digitOne = (0 until 9).map(i => (10 - i) * number(i).asDigit).sum
    digitOne = math.floorMod(math.floorMod(11 - digitOne, 11), 10)
    var digitTwo = (0 until 9).map(i => (11 - i) * number(i).asDigit).sum + 2 * digitOne
    digitTwo = math.floorMod(math.floorMod(11 - digitTwo, 11), 10)
    s"$digitOne$digitTwo"
}
