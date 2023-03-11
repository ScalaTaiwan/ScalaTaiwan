import zio.*
import zio.prelude.*

object Meetup extends App:
  type Rng = Int

  def nextLong(state: Rng): (Rng, Long) = {
    (
      state * 6_179_087 % 611_953,
      state * 14_356_033 % 1_299_709
    )
  }

  val st0 = 123456

  val (st1, rnd1) = nextLong(st0)
  val (st2, rnd2) = nextLong(st1)
  val (st3, rnd3) = nextLong(st2)

object rndState {
  // ZPure[...]
  type Rng = Int

  val nextLong: State[Rng, Long] =
    State.modify(s =>
      (
        s * 6_179_087 % 611_953,
        s * 14_356_033 % 1_299_709
      )
    )

  val rnds: State[Rng, Long] =
    for
      rnd1 <- nextLong
      rnd2 <- nextLong
      rnd3 <- nextLong
    yield rnd1 + rnd2 + rnd3

  val result = rnds.runResult(123456)

}
