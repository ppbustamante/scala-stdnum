import cl.mixin.stdnum.ar.CBU

class CBUSuite extends munit.FunSuite {
  test("format valid CBU") {
    val formattedCbu = CBU.format("2850590940090418135201")
    assertEquals(formattedCbu, "28505909 40090418135201")
  }

  test("compact valid CBU") {
    val compactedCbu = CBU.compact("2850590940090418135201")
    assertEquals(compactedCbu, "2850590940090418135201")
  }

  test("validate valid CBU") {
    val validCbu = CBU.validate("2850590940090418135201")
    assert(validCbu.isRight)
  }

  test("validate invalid CBU") {
    val validCbu = CBU.validate("2810590940090418135201")
    assert(validCbu.isLeft)
  }

  test("isValid valid CBU") {
    val validCbu = CBU.isValid("2850590940090418135201")
    assert(validCbu)
  }
}
