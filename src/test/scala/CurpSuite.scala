import cl.mixin.stdnum.mx.CURP

class CURPSuite extends munit.FunSuite {
  test("format valid CURP") {
    val formattedCurp = CURP.format("BOXW310820HNERXN09")
    assertEquals(formattedCurp, "BOXW310820HNERXN09")
  }

  test("compact valid CURP") {
    val compactedCurp = CURP.compact("BOXW310820HNERXN09")
    assertEquals(compactedCurp, "BOXW310820HNERXN09")
  }

  test("validate valid CURP") {
    val validCupr = CURP.validate("BOXW310820HNERXN09")
    assert(validCupr.isRight)
  }

  test("validate invalid CURP") {
    val validCurp = CURP.validate("BOXW310820HNERXN08")
    assert(validCurp.isLeft)
  }

  test("isValid valid CURP") {
    val validCurp = CURP.isValid("BOXW310820HNERXN09")
    assert(validCurp)
  }
}
