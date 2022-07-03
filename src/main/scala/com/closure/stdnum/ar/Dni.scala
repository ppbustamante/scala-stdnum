package com.closure.stdnum.ar

import com.closure.stdnum.exceptions.{
  ValidationError,
  InvalidFormat,
  InvalidLength
}
import com.closure.stdnum.utils.Utils

/** DNI (Documento Nacional de Identidad, Argentinian national identity nr.). */
object DNI {

  /** Convert the number to the minimal representation. This strips the number
    * of any valid separators and removes surrounding whitespace.
    */
  def compact(number: String): String =
    Utils.clean(number, Vector(' ', '-', '.')).toUpperCase.strip

  /** Check if the number is a valid DNI. This checks the length, formatting and
    * check digit.
    */
  def validate(number: String): Either[ValidationError, String] =
    val compactNumber = this.compact(number)
    if !Utils.isDigits(compactNumber) then Left(InvalidFormat())
    else if compactNumber.length != 7 && compactNumber.length != 8 then
      Left(InvalidLength())
    else Right(compactNumber)

  /** Check if the number is a valid DNI. */
  def isValid(number: String): Boolean = this.validate(number).isRight

  /** Reformat the number to the standard presentation format. */
  def format(number: String): String =
    val fNumber = this.compact(number)
    s"${fNumber.dropRight(6)}.${fNumber.dropRight(3).takeRight(3)}.${fNumber.takeRight(3)}"
}
