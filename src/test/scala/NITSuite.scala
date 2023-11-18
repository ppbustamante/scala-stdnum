import cl.mixin.stdnum.co.NIT

class NITSuite extends munit.FunSuite {
  test("format valid NIT") {
    val formattedNit = NIT.format("2131234321")
    assertEquals(formattedNit, "213.123.432-1")
  }

  test("compact valid NIT") {
    val compactedNit = NIT.compact("213.123.432-1")
    assertEquals(compactedNit, "2131234321")
  }

  test("validate valid NIT") {
    val validNit = NIT.validate("213.123.432-1")
    assert(validNit.isRight)
  }

  test("validate invalid NIT") {
    val validNit = NIT.validate("2131234325")
    assert(validNit.isLeft)
  }

  test("isValid valid NIT") {
    val validNit = NIT.isValid("2131234321")
    assert(validNit)
  }
}
