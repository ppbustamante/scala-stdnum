import cl.mixin.stdnum.ar.CUIT

class CUITSuite extends munit.FunSuite {
  test("format valid CUIT") {
    val formattedCuit = CUIT.format("20267565393")
    assertEquals(formattedCuit, "20-26756539-3")
  }

  test("compact valid CUIT") {
    val compactedCuit = CUIT.compact("20-26756539-3")
    assertEquals(compactedCuit, "20267565393")
  }

  test("validate valid CUIT") {
    val validCuit = CUIT.validate("20-05536168-2")
    assert(validCuit.isRight)
  }

  test("validate invalid CUIT") {
    val invalidCuit = CUIT.validate("20-26756539-2")
    assert(invalidCuit.isLeft)
  }

  test("isValid valid CUIT") {
    val isValidCuit = CUIT.isValid("20-05536168-2")
    assert(isValidCuit)
  }
}
