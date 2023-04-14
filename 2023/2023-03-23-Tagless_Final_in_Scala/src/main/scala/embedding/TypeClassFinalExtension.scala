package embedding

object TypeClassFinalExtension {
  import TypeClassFinal.*

  trait MulSYN[repr]:
    def mul(e1: repr, e2: repr): repr

  given MulSYN[Int] with
    def mul(e1: Int, e2: Int) = e1 * e2

  given MulSYN[String] with
    def mul(e1: String, e2: String) = s"($e1*$e2)"

  def tf2[A](using exp: ExpSYM[A], mul: MulSYN[A]): A = mul.mul(exp.lit(8), exp.neg(mul.mul(exp.lit(1), exp.lit(2))))

  def main(args: Array[String]): Unit = {
    println("-" * 15)
    println(s"tf2_eval: ${eval(tf2)}")
    println(s"tf2_view: ${view(tf2)}")
    println("-" * 15)
  }
}
