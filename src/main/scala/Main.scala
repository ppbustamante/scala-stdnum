import com.closure.stdnum.cl.RUT
import com.closure.stdnum.ar.DNI

@main def hello: Unit =
  println { RUT.isValid("18000612-5") }
  println { RUT.format("180006125") }
  println { DNI.isValid("20.123.456") }
  println { DNI.format("20123456") }
