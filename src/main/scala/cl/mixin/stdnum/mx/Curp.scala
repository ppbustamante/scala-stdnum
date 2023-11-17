package cl.mixin.stdnum.mx

import cl.mixin.stdnum.InvalidChecksum
import cl.mixin.stdnum.InvalidComponent
import cl.mixin.stdnum.InvalidFormat
import cl.mixin.stdnum.InvalidLength
import cl.mixin.stdnum.Tools
import cl.mixin.stdnum.ValidationError
import java.util.Date
import scala.util.Try

/** CURP (Clave Única de Registro de Población, Mexican personal ID).
  *
  * The Clave Única de Registro de Población (Population Registry Code) is unique identifier for
  * both citizens and residents of Mexico. The is an 18-character alphanumeric that contains certain
  * letters from the person's name, their gender and birth date and a check digit.
  *
  * More information:
  *   - https://en.wikipedia.org/wiki/CURP
  *   - https://www.gob.mx/curp
  */
object Curp {
  // these values should not appear as first part
  private val NAME_BLACKLIST = Vector(
    "BACA",
    "BAKA",
    "BUEI",
    "BUEY",
    "CACA",
    "CACO",
    "CAGA",
    "CAGO",
    "CAKA",
    "CAKO",
    "COGE",
    "COGI",
    "COJA",
    "COJE",
    "COJI",
    "COJO",
    "COLA",
    "CULO",
    "FALO",
    "FETO",
    "GETA",
    "GUEI",
    "GUEY",
    "JETA",
    "JOTO",
    "KACA",
    "KACO",
    "KAGA",
    "KAGO",
    "KAKA",
    "KAKO",
    "KOGE",
    "KOGI",
    "KOJA",
    "KOJE",
    "KOJI",
    "KOJO",
    "KOLA",
    "KULO",
    "LILO",
    "LOCA",
    "LOCO",
    "LOKA",
    "LOKO",
    "MAME",
    "MAMO",
    "MEAR",
    "MEAS",
    "MEON",
    "MIAR",
    "MION",
    "MOCO",
    "MOKO",
    "MULA",
    "MULO",
    "NACA",
    "NACO",
    "PEDA",
    "PEDO",
    "PENE",
    "PIPI",
    "PITO",
    "POPO",
    "PUTA",
    "PUTO",
    "QULO",
    "RATA",
    "ROBA",
    "ROBE",
    "ROBO",
    "RUIN",
    "SENO",
    "TETA",
    "VACA",
    "VAGA",
    "VAGO",
    "VAKA",
    "VUEI",
    "VUEY",
    "WUEI",
    "WUEY"
  )

  // these are valid two-character states
  private val VALID_STATES = Vector(
    "AS",
    "BC",
    "BS",
    "CC",
    "CH",
    "CL",
    "CM",
    "CS",
    "DF",
    "DG",
    "GR",
    "GT",
    "HG",
    "JC",
    "MC",
    "MN",
    "MS",
    "NE",
    "NL",
    "NT",
    "OC",
    "PL",
    "QR",
    "QT",
    "SL",
    "SP",
    "SR",
    "TC",
    "TL",
    "TS",
    "VZ",
    "YN",
    "ZS"
  )

  // characters used for checksum calculation,
  private val ALPHABET = "0123456789ABCDEFGHIJKLMN&OPQRSTUVWXYZ"

  /** Split the date parts from the number and return the birth date.
    *
    * @param number
    * @return
    *   Try of Date
    */
  private def getBirthDate(number: String): Try[Date] =
    Try {
      val compatNumber = this.compact(number)
      var year = compatNumber.drop(4).take(2).toInt
      val month = compatNumber.drop(6).take(2).toInt
      val day = compatNumber.drop(8).take(2).toInt
      if compatNumber(16).isDigit then year += 1900
      else year += 2000
      Date(year, month, day)
    }

  /** Get the gender (M/F) from the person's CURP.
    *
    * @param number
    *   CURP number
    * @return
    *   gender
    */
  def getGender(number: String): Option[Char] =
    val compatNumber = this.compact(number)
    if compatNumber(10) == 'H' then Option('M')
    else if compatNumber(10) == 'M' then Option('F')
    else None

  /** Convert the number to the minimal representation. This strips the number of any valid
    * separators and removes surrounding whitespace.
    */
  def compact(number: String): String =
    Tools.clean(number, Vector('-', '_', ' ')).toUpperCase.strip

  /** Check if the number is a valid CURP. */
  def isValid(number: String): Boolean = this.validate(number).isRight

  /** Check if the number is a valid CUPR. This checks the length, formatting and check digit. */
  def validate(
    number: String,
    validateCheckDigits: Boolean = true
  ): Either[ValidationError, String] =
    val compatNumber = this.compact(number)
    if compatNumber.length != 18 then Left(InvalidLength())
    else if !"^[A-Z]{4}[0-9]{6}[A-Z]{6}[0-9A-Z][0-9]$".r.matches(compatNumber) then
      Left(InvalidFormat())
    else if NAME_BLACKLIST.contains(compatNumber.take(4)) then Left(InvalidComponent())
    else if this.getBirthDate(compatNumber).isFailure then Left(InvalidComponent())
    else if this.getGender(compatNumber).isEmpty then Left(InvalidComponent())
    else if !VALID_STATES.contains(compatNumber.drop(11).take(2)) then Left(InvalidComponent())
    else if validateCheckDigits && compatNumber.last.toString() != this.calcCheckDigit(compatNumber)
    then Left(InvalidChecksum())
    else Right(compatNumber)

  /** Reformat the number to the standard presentation format. */
  def format(number: String): String = this.compact(number)

  /** Calculate the check digit. The number passed should not have the check digit included. */
  private def calcCheckDigit(number: String): String =
    /** check = sum(_alphabet.index(c) * (18 - i) for i, c in enumerate(number[:17])) return str((10
      * \- check % 10) % 10)
      */
    val check = number
      .take(17)
      .zipWithIndex
      .map((char: Char, index: Int) => ALPHABET.indexOf(char) * (18 - index))
      .sum
    ((10 - (check % 10)) % 10).toString
}
