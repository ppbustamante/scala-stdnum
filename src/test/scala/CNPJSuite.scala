import cl.mixin.stdnum.br.CNPJ

class CNPJSuite extends munit.FunSuite {
  test("format valid CNPJ") {
    val formattedCnpj = CNPJ.format("16727230000197")
    assertEquals(formattedCnpj, "16.727.230/0001-97")
  }

  test("compact valid CNPJ") {
    val compactedCnpj = CNPJ.compact("16.727.230/0001-97")
    assertEquals(compactedCnpj, "16727230000197")
  }

  test("validate valid CNPJ") {
    val validCnpj = CNPJ.validate("16.727.230/0001-97")
    println(validCnpj)
    assert(validCnpj.isRight)
  }

  test("validate invalid CNPJ") {
    val invalidCnpj = CNPJ.validate("16.727.230.0001-98")
    assert(invalidCnpj.isLeft)
  }

  test("validate another invalid CNPJ") {
    val invalidCnpj = CNPJ.validate("16.727.230/0001=97")
    assert(invalidCnpj.isLeft)
  }

  test("isValid valid CNPJ") {
    val validCnpj = CNPJ.isValid("16.727.230/0001-97")
    assert(validCnpj)
  }
}
