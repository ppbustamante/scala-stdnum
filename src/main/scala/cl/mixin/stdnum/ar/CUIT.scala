package cl.mixin.stdnum.ar

import cl.mixin.stdnum.Identity
import cl.mixin.stdnum.InvalidChecksum
import cl.mixin.stdnum.InvalidComponent
import cl.mixin.stdnum.InvalidFormat
import cl.mixin.stdnum.InvalidLength
import cl.mixin.stdnum.Tools
import cl.mixin.stdnum.ValidationError

/** CUIT (Código Único de Identificación Tributaria, Argentinian tax number). */
object CUIT extends Identity {
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

  override def compact(number: String): String =
    Tools.clean(number, Vector(' ', '-'))

  override def format(number: String, separator: String = "-"): String =
    val fNumber = this.compact(number)
    s"${fNumber.take(2)}$separator${fNumber.slice(2, 10)}$separator${fNumber.drop(10)}"

  override def validate(
    number: String,
    validateCheckDigit: Boolean = true
  ): Either[ValidationError, String] =
    val compactNumber = this.compact(number)
    if compactNumber.length != 11 then Left(InvalidLength())
    else if !Tools.isDigits(compactNumber) then Left(InvalidFormat())
    else if !CUIT_TYPES.contains(compactNumber.take(2)) then Left(InvalidComponent())
    else if this.calcCheckDigit(compactNumber.init) != compactNumber.last then
      Left(InvalidChecksum())
    else Right(compactNumber)

  /** Calculate the check digit. The number passed should not have the check digit included.
    */
  private def calcCheckDigit(number: String): Char =
    val weights = Vector(5, 4, 3, 2, 7, 6, 5, 4, 3, 2)
    val check =
      math.floorMod(weights.zip(number).map((w, n) => w * n.asDigit).sum, 11)
    "012345678990" (11 - check)
}
