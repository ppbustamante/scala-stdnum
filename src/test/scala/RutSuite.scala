import com.closure.stdnum.cl.Rut

class RutSuite extends munit.FunSuite {
  test("format valid RUT") {
    val formattedRut = Rut.format("30866897-5")
    assertEquals(formattedRut, "30.866.897-5")
  }

  test("compact valid RUT") {
    val compactedRut = Rut.compact("30.866.897-5")
    assertEquals(compactedRut, "308668975")
  }

  test("validate valid RUT") {
    val validRut = Rut.validate("30.866.897-5")
    assert(validRut.isRight)
  }

  test("isValid valid RUT") {
    val validRut = Rut.isValid("30.866.897-5")
    assert(validRut)
  }
}
