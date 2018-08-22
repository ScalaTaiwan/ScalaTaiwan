import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object Callback extends App {
  val futureOne = Future {
    Thread.sleep(1000)
    1
  }

  val futureTwo = Future {
    Thread.sleep(2000)
    2
  }

  futureOne.onComplete {
    case Success(s1) =>
      futureTwo.onComplete {
        case Success(s2) => println(s1 + s2)
        case Failure(f2) => println(s"error, $f2")
      }
    case Failure(f1) => println(s"error, $f1")
  }

  Thread.sleep(5000)
}
