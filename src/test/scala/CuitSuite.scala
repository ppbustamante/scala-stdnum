import cl.mixin.stdnum.ar.Cuit

class CuitSuite extends munit.FunSuite {
  test("format valid CUIT") {
    val formattedCuit = Cuit.format("20267565393")
    assertEquals(formattedCuit, "20-26756539-3")
  }

  test("compact valid CUIT") {
    val compactedCuit = Cuit.compact("20-26756539-3")
    assertEquals(compactedCuit, "20267565393")
  }

  test("validate valid CUIT") {
    val validCuit = Cuit.validate("20-05536168-2")
    assert(validCuit.isRight)
  }

  test("validate invalid CUIT") {
    val invalidCuit = Cuit.validate("20-26756539-2")
    println(invalidCuit)
    assert(invalidCuit.isLeft)
  }

  test("isValid valid CUIT") {
    val isValidCuit = Cuit.isValid("20-05536168-2")
    assert(isValidCuit)
  }
}
