import java.util.concurrent.{Executors, TimeUnit}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

object BlockingKeyword extends App {
  Executors.newSingleThreadScheduledExecutor.schedule(Sentinel, 3000, TimeUnit.MILLISECONDS)

//  val executorService = Executors.newFixedThreadPool(4)
//  implicit val ec     = ExecutionContext.fromExecutorService(executorService)
  for (_ <- 0 until 100) {
    Future {
      blocking {
        Thread.sleep(3000)
      }
    }
  }
}
