package cl.mixin.stdnum.br

import cl.mixin.stdnum.{
  Validator,
  InvalidChecksum,
  InvalidFormat,
  InvalidLength,
  Tools,
  ValidationError
}

/** CNPJ (Cadastro Nacional da Pessoa Jurídica, Brazilian company identifier).
  *
  * Numbers from the national register of legal entities have 14 digits. The first 8 digits identify
  * the company, the following 4 digits identify a business unit and the last 2 digits are check
  * digits.
  */
object CNPJ extends Validator {
  // clean(number, ' -./').strip()
  override def compact(number: String): String = Tools.clean(number, Vector(' ', '.', '/', '-'))

  override def format(number: String, separator: String = "."): String =
    val compactNumber = this.compact(number)
    s"${compactNumber.take(2)}$separator${compactNumber
        .slice(2, 5)}$separator${compactNumber
        .slice(5, 8)}/${compactNumber.slice(8, 12)}-${compactNumber.drop(12)}"

  override def validate(
    number: String,
    validateCheckDigit: Boolean
  ): Either[ValidationError, String] =
    val compactNumber = this.compact(number)
    if !Tools.isDigits(compactNumber) || compactNumber.toLong <= 0 then Left(InvalidFormat())
    else if compactNumber.length != 14 then Left(InvalidLength())
    else if validateCheckDigit && this.calcCheckDigits(compactNumber) != compactNumber.takeRight(2)
    then Left(InvalidChecksum())
    else Right(compactNumber)

  private def calcCheckDigits(number: String): String =
    val digitOne =
      math.floorMod(
        math.floorMod(
          11 - number
            .take(12)
            .zipWithIndex
            .map((c, i) => (math.floorMod(3 - i, 8) + 2) * c.asDigit)
            .sum,
          11
        ),
        10
      )
    val digitTwo = math.floorMod(
      math.floorMod(
        11 - number
          .take(12)
          .zipWithIndex
          .map((c, i) => (math.floorMod(4 - i, 8) + 2) * c.asDigit)
          .sum - 2 * digitOne,
        11
      ),
      10
    )
    s"$digitOne$digitTwo"
}
