package cl.mixin.stdnum

/** Top-level error for validating numbers. This exception should normally not be raised, only
  * subclasses of this exception.
  */
sealed trait ValidationError extends Exception

/** Something is wrong with the format of the number. This generally means characters or delimiters
  * that are not allowed are part of the number or required parts are missing.
  */
class InvalidFormat() extends ValidationError

/** The number's internal checksum or check digit does not match.
  */
case class InvalidChecksum() extends ValidationError

/** The number's internal checksum or check digit does not match.
  */
case class InvalidComponent() extends ValidationError

/** The length of the number is wrong.
  */
case class InvalidLength() extends InvalidFormat
