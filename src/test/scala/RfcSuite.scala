import cl.mixin.stdnum.mx.RFC

class RFCSuite extends munit.FunSuite {
  test("format valid RFC") {
    val formattedRfc = RFC.format("GODE561231GR8")
    assertEquals(formattedRfc, "GODE 561231 GR8")
  }

  test("compact valid RFC") {
    val compactedRfc = RFC.compact("MAB-930714-8T4")
    assertEquals(compactedRfc, "MAB9307148T4")
  }

  test("validate valid RFC") {
    val validPersonalRfc = RFC.validate("GODE 561231 GR8") // personal number
    val validCompanyRfc = RFC.validate("MAB-930714-8T4") // company number
    val validPersonalWithoutSerialRfc = RFC.validate("COMG600703") // personal number without serial
    assert(validPersonalRfc.isRight)
    assert(validCompanyRfc.isRight)
    assert(validPersonalWithoutSerialRfc.isRight)
  }

  test("validate invalid RFC") {
    val invalidRfc = RFC.validate("VACE-460910-SX6")
    assert(invalidRfc.isLeft)
  }

  test("isValid valid RFC") {
    val validRfc = RFC.isValid("MAB-930714-8T4")
    assert(validRfc)
  }
}
