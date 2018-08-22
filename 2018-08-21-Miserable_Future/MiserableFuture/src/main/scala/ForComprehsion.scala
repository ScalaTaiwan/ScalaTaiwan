import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object ForComprehsion extends App {
  val futureOne = Future {
    Thread.sleep(1000)
    1
  }

  val futureTwo = Future {
    Thread.sleep(2000)
    2
  }

  val result = for {
    r1 <- futureOne
    r2 <- futureTwo
  } yield {
    println(r1 + r2)
  }

  Await.result(result, 5 second)
}
