import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Random
//import scala.concurrent.duration._
import org.scalameter.{Bench, Gen}

object FutureBenchmark extends Bench.LocalTime {

  val sizes: Gen[Int] = Gen.range("size")(3000, 15000, 3000)

  val ranges: Gen[Range] = for {
    size <- sizes
  } yield 0 until size

  performance of "Future" in {
    measure method "future" in {
      using(ranges) in { r =>
        r.map(future)
      }
    }

    measure method "futureWithSuccessful" in {
      using(ranges) in { r =>
        r.map(futureWithSuccessful)
      }
    }
  }

  def future(x: Int): Future[Int] =
    for {
      r1 <- Future(x + Random.nextInt())
      r2 <- Future(r1 - Random.nextInt())
      r3 <- Future(r2 * Random.nextInt())
      r4 <- Future(r3 / Random.nextInt())
    } yield {
      r4
    }

  def futureWithSuccessful(x: Int): Future[Int] =
    for {
      r1 <- Future.successful(x + Random.nextInt())
      r2 <- Future.successful(r1 - Random.nextInt())
      r3 <- Future.successful(r2 * Random.nextInt())
      r4 <- Future.successful(r3 / Random.nextInt())
    } yield {
      r4
    }
}
