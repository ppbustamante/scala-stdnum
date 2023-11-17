import cl.mixin.stdnum.mx.Curp

class CurpSuite extends munit.FunSuite {
  test("format valid CURP") {
    val formattedCurp = Curp.format("BOXW310820HNERXN09")
    assertEquals(formattedCurp, "BOXW310820HNERXN09")
  }

  test("compact valid CURP") {
    val compactedCurp = Curp.compact("BOXW310820HNERXN09")
    assertEquals(compactedCurp, "BOXW310820HNERXN09")
  }

  test("validate valid CURP") {
    val validCupr = Curp.validate("BOXW310820HNERXN09")
    assert(validCupr.isRight)
  }

  test("validate invalid CURP") {
    val validCurp = Curp.validate("BOXW310820HNERXN08")
    assert(validCurp.isLeft)
  }

  test("isValid valid CURP") {
    val validCurp = Curp.isValid("BOXW310820HNERXN09")
    assert(validCurp)
  }
}
