import cl.mixin.stdnum.br.CPF

class CPFSuite extends munit.FunSuite {
  test("format valid CPF") {
    val formattedCpf = CPF.format("39053344705")
    assertEquals(formattedCpf, "390.533.447-05")
  }

  test("compact valid CPF") {
    val compactedCpf = CPF.compact("390.533.447-05")
    assertEquals(compactedCpf, "39053344705")
  }

  test("validate valid CPF") {
    val validCpf = CPF.validate("390.533.447-05")
    assert(validCpf.isRight)
  }

  test("validate invalid CPF") {
    val invalidCpf = CPF.validate("231.002.999-00")
    assert(invalidCpf.isLeft)
  }

  test("validate another invalid CPF") {
    val invalidCpf = CPF.validate("390.533.447=0")
    assert(invalidCpf.isLeft)
  }

  test("isValid valid CPF") {
    val validCpf = CPF.isValid("390.533.447-05")
    assert(validCpf)
  }
}
