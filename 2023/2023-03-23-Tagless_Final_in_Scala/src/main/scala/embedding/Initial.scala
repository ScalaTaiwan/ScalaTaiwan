package embedding

object Initial {

  trait Exp
  case class Lit(v: Int) extends Exp
  case class Neg(exp: Exp) extends Exp
  case class Add(exp1: Exp, exp2: Exp) extends Exp

  val ti1 = Add(Lit(8), (Neg (Add(Lit(1), Lit(2)))))

  def eval(exp: Exp): Int = exp match {
    case Lit(n) => n
    case Neg(e) => -eval(e)
    case Add(e1, e2) => eval(e1) + eval(e2)
  }

  def view(exp: Exp): String = exp match {
    case Lit(n) => s"$n"
    case Neg(e) => "(-" ++ view(e) ++ ")"
    case Add(e1, e2) => "(" ++ view(e1) ++ "+" ++ view(e2) ++ ")"
  }

  val ti1_eval = eval(ti1)
  val ti1_view = view(ti1)

  def main(args: Array[String]): Unit = {
    println("-" * 15)
    println(s"ti1_eval: $ti1_eval")
    println(s"ti1_view: $ti1_view")
    println("-" * 15)
  }
}


