package stdnum.cl

import stdnum.{InvalidChecksum, InvalidFormat, InvalidLength, Tools, ValidationError, Validator}

/** RUT (Rol Único Tributario, Chilean national tax number).
  *
  * The RUT, the Chilean national tax number is the same as the RUN (Rol Único Nacional) the Chilean
  * national identification number. The number consists of 8 digits, followed by a check digit.
  */
object RUT extends Validator {

  /** Reformat the number to the standard presentation format. */
  override def format(number: String, separator: String = " "): String =
    val fNumber = this.compact(number)
    s"${fNumber.dropRight(7)}.${fNumber.dropRight(4).takeRight(3)}.${fNumber.init
        .takeRight(3)}-${fNumber.last}"

  /** Check if the number is a valid RUT. This checks the length, formatting and check digit.
    */
  override def validate(
    number: String,
    validateCheckDigit: Boolean = true
  ): Either[ValidationError, String] =
    val compactNumber = this.compact(number)
    if compactNumber.length != 8 && compactNumber.length != 9 then Left(InvalidLength())
    else if !Tools.isDigits(compactNumber.init) then Left(InvalidFormat())
    else if validateCheckDigit && this.calcCheckDigits(compactNumber.init) != compactNumber.last
    then Left(InvalidChecksum())
    else Right(compactNumber)

  /** Convert the number to the minimal representation. This strips the number of any valid
    * separators and removes surrounding whitespace.
    */
  override def compact(number: String): String =
    val cleanedNumber =
      Tools.clean(number, Vector(' ', '-', '.'))
    if cleanedNumber.startsWith("CL") then cleanedNumber.drop(2)
    else cleanedNumber

  /** Calculate the check digit. The number passed should not have the check digit included.
    */
  private def calcCheckDigits(number: String): Char =
    val rut = number.toVector
    val modulus: Int = 11 - ((rut.map(i => i.asDigit) zip rut.indices
      .map(i => i % 6 + 2)
      .reverse).map(i => i._1 * i._2).sum % 11)
    if modulus == 11 then '0'
    else if modulus == 10 then 'K'
    else (modulus + 48).toChar
}
