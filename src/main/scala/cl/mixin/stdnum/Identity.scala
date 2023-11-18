package cl.mixin.stdnum

trait Identity {

  /** Convert the number to the minimal representation. This strips the number of any valid
    * separators and removes surrounding whitespace.
    */
  def compact(number: String): String

  /** Reformat the number to the standard presentation format. */
  def format(number: String, separator: String = ""): String

  /** Check if the number is a valid identity. */

  def isValid(number: String): Boolean = this.validate(number).isRight

  /** Check if the number is a valid identity. This checks the length, formatting and check digit.
    */
  def validate(number: String, validateCheckDigit: Boolean = true): Either[ValidationError, String]
}
