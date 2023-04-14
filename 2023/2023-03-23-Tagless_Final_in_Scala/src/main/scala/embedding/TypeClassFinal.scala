package embedding

object TypeClassFinal {

  // the language (algebra)
  trait ExpSYM[repr]:
    def lit(n: Int): repr
    def neg(e: repr): repr
    def add(e1: repr , e2: repr): repr

  given ExpSYM[Int] with
    def lit(n: Int) = n
    def neg(e: Int) = -e
    def add(e1: Int, e2: Int) = e1 + e2

  given ExpSYM[String] with
    def lit(n: Int) = s"$n"
    def neg(e: String) = s"(-$e)"
    def add(e1: String, e2: String) = s"($e1+$e2)" 

  def tf1[A](using exp: ExpSYM[A]): A = exp.add(exp.lit(8), exp.neg(exp.add(exp.lit(1), exp.lit(2))))


  // the interpreters
  def eval(in: Int): Int = identity(in)
  def view(str: String): String = identity(str)

  def main(args: Array[String]): Unit = {
    println("-" * 15)
    println(s"tf1_eval: ${eval(tf1)}")
    println(s"tf1_view: ${view(tf1)}")
    println("-" * 15)
    println(s"tf1_as_Int: ${tf1[Int]}")
    println(s"tf1_as_String: ${tf1[String]}")
    println("-" * 15)
  }

}
