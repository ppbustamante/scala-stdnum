import cl.mixin.stdnum.cl.RUT

class RUTSuite extends munit.FunSuite {
  test("format valid RUT") {
    val formattedRut = RUT.format("30866897-5")
    assertEquals(formattedRut, "30.866.897-5")
  }

  test("compact valid RUT") {
    val compactedRut = RUT.compact("30.866.897-5")
    assertEquals(compactedRut, "308668975")
  }

  test("validate valid RUT") {
    val validRut = RUT.validate("18.000.612-5")
    assert(validRut.isRight)
  }

  test("validate invalid RUT") {
    val validRut = RUT.validate("30.866.497-5")
    assert(validRut.isLeft)
  }

  test("isValid valid RUT") {
    val validRut = RUT.isValid("30.866.897-5")
    assert(validRut)
  }
}
