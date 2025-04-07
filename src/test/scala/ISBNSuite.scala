import stdnum.ISBN

class ISBNSuite extends munit.FunSuite {
  test("format valid ISBN") {
    val formattedISDBN = ISBN.format("978-0-13-041717-6")
    assertEquals(formattedISDBN, "978-0-13-041717-6")
  }

  test("compact valid ISBN") {
    val compactedISBN = ISBN.compact("978-0-13-041717-6")
    assertEquals(compactedISBN, "9780130417176")
  }

  test("validate valid ISBN") {
    val validISBN = ISBN.validate("978-8445007068")
    println(validISBN)
    assert(validISBN.isRight)
  }

  test("validate invalid ISBN") {
    val invalidISBN = ISBN.validate("978-0-13-042717-6")
    assert(invalidISBN.isLeft)
  }

  test("isValid valid ISBN") {
    val validISBN = ISBN.isValid("978-0-13-041717-6")
    assert(validISBN)
  }
}
