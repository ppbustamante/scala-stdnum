import cl.mixin.stdnum.EAN

class EANuite extends munit.FunSuite {
  test("format valid EAN") {
    val formattedEAN = EAN.format("978-0-13-041717-6")
    assertEquals(formattedEAN, "978-0-13-041717-6")
  }

  test("compact valid EAN") {
    val compactedEAN = EAN.compact("978-0-13-041717-6")
    assertEquals(compactedEAN, "9780130417176")
  }

  test("validate valid EAN") {
    val validEAN = EAN.validate("978-8445007068")
    assert(validEAN.isRight)
  }

  test("validate invalid EAN") {
    val invalidEAN = EAN.validate("978-0-13-042717-6")
    assert(invalidEAN.isLeft)
  }

  test("isValid valid EAN") {
    val validEAN = EAN.isValid("978-0-13-041717-6")
    assert(validEAN)
  }
}
