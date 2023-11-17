package cl.mixin.stdnum.ar

import cl.mixin.stdnum.InvalidChecksum
import cl.mixin.stdnum.InvalidComponent
import cl.mixin.stdnum.InvalidFormat
import cl.mixin.stdnum.InvalidLength
import cl.mixin.stdnum.Tools
import cl.mixin.stdnum.ValidationError

/** CUIT (Código Único de Identificación Tributaria, Argentinian tax number). */
object Cuit {
  private val CUIT_TYPES = Vector(
    "20",
    "23",
    "24",
    "27", // individuals
    "30",
    "33",
    "34", // companies
    "50",
    "51",
    "55" // international purposes
  )

  /** Check if the number is a valid CUIT. */
  def isValid(number: String): Boolean = this.validate(number).isRight

  /** Reformat the number to the standard presentation format. */
  def format(number: String): String =
    val fNumber = this.compact(number)
    s"${fNumber.take(2)}-${fNumber.drop(2).take(8)}-${fNumber.drop(10)}"

  /** Check if the number is a valid CUIT. This checks the length, formatting and check digit.
    */
  def validate(number: String): Either[ValidationError, String] =
    val compatNumber = this.compact(number)
    if compatNumber.length != 11 then Left(InvalidLength())
    else if !Tools.isDigits(compatNumber) then Left(InvalidFormat())
    else if !CUIT_TYPES.contains(compatNumber.take(2)) then Left(InvalidComponent())
    else if this.calcCheckDigit(number) != compatNumber.last then Left(InvalidChecksum())
    else Right(compatNumber)

  /** Convert the number to the minimal representation. This strips the number of any valid
    * separators and removes surrounding whitespace.
    */
  def compact(number: String): String =
    Tools.clean(number, Vector(' ', '-')).toUpperCase.strip

  /** Calculate the check digit. The number passed should not have the check digit included.
    */
  private def calcCheckDigit(number: String): Char =
    val weights = Vector(5, 4, 3, 2, 7, 6, 5, 4, 3, 2)
    val check =
      weights.zip(number).map((w, n) => w * n.toInt).reduce(_ + _) % 11
    "012345678990" (11 - check)
}
