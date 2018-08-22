import java.util.concurrent.{Executors, TimeUnit}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

object NoBlockingKeyword extends App {
  Executors.newSingleThreadScheduledExecutor.schedule(Sentinel, 3000, TimeUnit.MILLISECONDS)

  for (_ <- 0 until 100) {
    Future {
      Thread.sleep(3000)
    }
  }
}
