import cl.mixin.stdnum.ve.RIF

class RIFSuite extends munit.FunSuite {
  test("format valid RIF") {
    val formattedRif = RIF.format("V114702834")
    assertEquals(formattedRif, "V-11470283-4")
  }

  test("compact valid RIF") {
    val compactedRif = RIF.compact("V-11470283-4")
    assertEquals(compactedRif, "V114702834")
  }

  test("validate valid RIF") {
    val validRif = RIF.validate("V-11470283-4")
    assert(validRif.isRight)
  }

  test("validate invalid RIF") {
    val invalidRif = RIF.validate("V-11470283-3")
    assert(invalidRif.isLeft)
  }

  test("isValid valid RIF") {
    val validRif = RIF.isValid("V-11470283-4")
    assert(validRif)
  }
}
