package com.closure.stdnum.cl

import com.closure.stdnum.exceptions.{
  ValidationError,
  InvalidLength,
  InvalidChecksum,
  InvalidFormat
}
import com.closure.stdnum.utils.Utils

/** RUT (Rol Único Tributario, Chilean national tax number). */
object RUT:
  /** Check if the number is a valid RUT. */
  def isValid(number: String): Boolean = this.validate(number).isRight

  /** Reformat the number to the standard presentation format. */
  def format(number: String): String =
    val fNumber = this.compact(number)
    s"${fNumber.dropRight(7)}.${fNumber.dropRight(4).takeRight(3)}.${fNumber.init
        .takeRight(3)}.-${fNumber.last}"

  /** Check if the number is a valid RUT. This checks the length, formatting and
    * check digit.
    */
  def validate(number: String): Either[ValidationError, String] =
    val compactNumber = this.compact(number)
    if compactNumber.length != 8 && compactNumber.length != 9 then
      Left(InvalidLength())
    else if !Utils.isDigits(compactNumber.init) then Left(InvalidFormat())
    else if this.calcCheckDigit(compactNumber.init) != compactNumber.last then
      Left(InvalidChecksum())
    else Right(compactNumber)

  /** Convert the number to the minimal representation. This strips the number
    * of any valid separators and removes surrounding whitespace.
    */
  def compact(number: String): String =
    val cleanedNumber =
      Utils.clean(number, Vector(' ', '-', '.')).toUpperCase.strip
    if cleanedNumber.startsWith("CL") then cleanedNumber.drop(2)
    else cleanedNumber

  /** Calculate the check digit. The number passed should not have the check
    * digit included.
    */
  def calcCheckDigit(number: String): Char =
    val rut = number.toVector
    val modulus: Int = 11 - (((rut.map(i => i.toInt - 48)) zip rut.indices
      .map(i => i % 6 + 2)
      .reverse).map(i => i._1 * i._2).foldLeft(0)((a, b) => a + b) % 11)
    if modulus == 11 then '0'
    else if modulus == 10 then 'K'
    else (modulus + 48).toChar
