package embedding

object InitialExtension {
  import embedding.{Initial => Old}

  trait Exp
  case class EOld(e: Old.Exp) extends Exp
  case class Mul(e1: Exp, e2: Exp) extends Exp

  val ti2 = Mul(EOld(Old.Lit(7)), EOld(Old.ti1))
  
  // val ti1_eval = Old.eval(ti2)
  // val ti1_view = Old.view(ti2)

  // Boom!

}
