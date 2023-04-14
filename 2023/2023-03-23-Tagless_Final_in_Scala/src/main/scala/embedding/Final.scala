package embedding

object Final {
  type Repr = Int

  def lit(n: Int): Repr = n
  def neg(e: Repr): Repr = -e
  def add(e1: Repr, e2: Repr): Repr = e1 + e2

  val tf1 = add(lit(8), neg(add(lit(1), lit(2))))

  def main(args: Array[String]): Unit = {
    println("-" * 15)
    println(s"tf1: $tf1")
    println("-" * 15)
  }
}
