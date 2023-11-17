import cl.mixin.stdnum.mx.Rfc

class RfcSuite extends munit.FunSuite {
  test("format valid RFC") {
    val formattedRfc = Rfc.format("GODE561231GR8")
    assertEquals(formattedRfc, "GODE 561231 GR8")
  }

  test("compact valid RFC") {
    val compactedRfc = Rfc.compact("MAB-930714-8T4")
    assertEquals(compactedRfc, "MAB9307148T4")
  }

  test("validate valid RFC") {
    val validPersonalRfc = Rfc.validate("GODE 561231 GR8") // personal number
    val validCompanyRfc = Rfc.validate("MAB-930714-8T4") // personal number
    val validPersonalWithoutSerialRfc = Rfc.validate("COMG600703") // personal number without serial
    val validRfc = Rfc.validate("VACE-460910-SX6") // personal number without serial
    assert(validPersonalRfc.isRight)
    assert(validCompanyRfc.isRight)
    assert(validPersonalWithoutSerialRfc.isRight)
    assert(validRfc.isRight)
  }

  test("validate invalid RFC") {
    val validRfc = Rfc.validate("VACE-460910-SX6", true)
    assert(validRfc.isLeft)
  }

  test("isValid valid RFC") {
    val validRfc = Rfc.isValid("VACE-460910-SX6")
    assert(validRfc)
  }
}
