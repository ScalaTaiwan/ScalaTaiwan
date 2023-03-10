import zio.prelude.*
import zio.prelude.fx.ZPure

object Prelude:

  @main def run =
    println(">>>> " + Amount.make(-10))

object rndExplicit:
  type Rng = Int

  def nextLong(s: Rng): (Rng, Long) =
    (s * 6_179_087 % 611_953, s * 14_356_033 % 1_299_709)

  val st0 = 951_161

  val (st1, rnd1) = nextLong(st0) //  638340
  val (st2, rnd2) = nextLong(st1) // -300208
  val (st3, rnd3) = nextLong(st2) // -213575

  val sum = rnd1 + rnd2 + rnd3

val am = Amount(10)

object Amount extends Newtype[Int]:
  override inline def assertion = Assertion.greaterThanOrEqualTo(0)

type Amount = Amount.Type

val x: Amount = Amount(10)
val y: Int = Amount.unwrap(x)

object A:
  opaque type Amount = Int
  val x: Amount = 5

object B:
  type Amount = Int

object C:
  def x(a: A.Amount) = ""
  // val u: A.Amount = 5
  // val a = x(10)

  def y(a: B.Amount) = ""
  val b = y(10)
