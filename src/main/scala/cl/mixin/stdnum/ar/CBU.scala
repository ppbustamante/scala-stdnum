package cl.mixin.stdnum.ar

import cl.mixin.stdnum.{
  Identity,
  InvalidChecksum,
  InvalidFormat,
  InvalidLength,
  Tools,
  ValidationError
}

/** CBU (Clave Bancaria Uniforme, Argentine bank account number).
  *
  * CBU it s a code of the Banks of Argentina to identify customer accounts. The number consists of
  * 22 digits and consists of a 3 digit bank identifier, followed by a 4 digit branch identifier, a
  * check digit, a 13 digit account identifier and another check digit.
  *
  * More information:
  *
  *   - https://es.wikipedia.org/wiki/Clave_Bancaria_Uniforme
  */
object CBU extends Identity {
  override def compact(number: String): String = Tools.clean(number, Vector(' ', '-'))

  override def format(number: String, separator: String = " "): String =
    s"${number.take(8)}$separator${number.drop(8)}"

  override def validate(
    number: String,
    validateCheckDigit: Boolean = true
  ): Either[ValidationError, String] =
    val compactNumber = this.compact(number)
    if compactNumber.length != 22 then Left(InvalidLength())
    else if !Tools.isDigits(compactNumber) then Left(InvalidFormat())
    else if this.calcCheckDigit(compactNumber.take(7)) != compactNumber(7).toString then
      Left(InvalidChecksum())
    else if this.calcCheckDigit(compactNumber.drop(8).init) != compactNumber.last.toString then
      Left(InvalidChecksum())
    else Right(compactNumber)

  private def calcCheckDigit(number: String): String =
    val weights = Vector(3, 1, 7, 9)
    val check = number.reverse.zipWithIndex.map((c, i) => c.asDigit * weights(i % 4)).sum
    math.floorMod(10 - check, 10).toString
}
