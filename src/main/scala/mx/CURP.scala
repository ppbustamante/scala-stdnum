package stdnum.mx

import java.util.{Date, GregorianCalendar}
import scala.util.Try
import stdnum.{
  InvalidChecksum,
  InvalidComponent,
  InvalidFormat,
  InvalidLength,
  Tools,
  ValidationError,
  Validator
}

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
object CURP extends Validator {
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

  private def getBirthDate(number: String): Try[Date] =
    Try {
      val compactNumber = this.compact(number)
      var year = compactNumber.slice(4, 6).toInt
      val month = compactNumber.slice(6, 8).toInt
      val day = compactNumber.slice(8, 10).toInt
      if compactNumber(16).isDigit then year += 1900
      else year += 2000
      GregorianCalendar(year, month, day).getGregorianChange
    }

  private def getGender(number: String): Option[Char] =
    val compactNumber = this.compact(number)
    if compactNumber(10) == 'H' then Option('M')
    else if compactNumber(10) == 'M' then Option('F')
    else None

  def compact(number: String): String =
    Tools.clean(number, Vector('-', '_', ' '))

  override def validate(
    number: String,
    validateCheckDigits: Boolean = true
  ): Either[ValidationError, String] =
    val compactNumber = this.compact(number)
    if compactNumber.length != 18 then Left(InvalidLength())
    else if !"^[A-Z]{4}[0-9]{6}[A-Z]{6}[0-9A-Z][0-9]$".r.matches(compactNumber) then
      Left(InvalidFormat())
    else if NAME_BLACKLIST.contains(compactNumber.take(4)) then Left(InvalidComponent())
    else if this.getBirthDate(compactNumber).isFailure then Left(InvalidComponent())
    else if this.getGender(compactNumber).isEmpty then Left(InvalidComponent())
    else if !VALID_STATES.contains(compactNumber.slice(11, 13)) then Left(InvalidComponent())
    else if validateCheckDigits && compactNumber.last.toString != this.calcCheckDigits(
          compactNumber
        )
    then Left(InvalidChecksum())
    else Right(compactNumber)

  override def format(number: String, separator: String = ""): String = this.compact(number)

  /** Calculate the check digit. The number passed should not have the check digit included. */
  private def calcCheckDigits(number: String): String =
    /** check = sum(_alphabet.index(c) * (18 - i) for i, c in enumerate(number[:17])) return str((10
      * \- check % 10) % 10)
      */
    val check = number
      .take(17)
      .zipWithIndex
      .map((char: Char, index: Int) => ALPHABET.indexOf(char) * (18 - index))
      .sum
    math.floorMod(10 - math.floorMod(check, 10), 10).toString
}
