import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Random

object Stock {
  def mapThenSequence(): Future[List[Double]] = {
    val stockIds: List[String]                = List.fill(Random.nextInt(1000))(Random.nextInt(1000).toString)
    val mapResults: List[Future[Double]]      = stockIds.map(getStockPrice)
    val sequenceResults: Future[List[Double]] = Future.sequence(mapResults)
    sequenceResults
  }

  private def getStockPrice(id: String): Future[Double] = Future {
    val price = Random.nextDouble()
    price
  }

  def traverse(): Future[List[Double]] = {
    val stockIds: List[String] = List.fill(Random.nextInt(1000))(Random.nextInt(1000).toString)

    // This is useful for performing a parallel map. For example, to apply a function to all items of a list
    // in parallel:
    val traverseResults: Future[List[Double]] = Future.traverse(stockIds)(getStockPrice)
    traverseResults
  }
}
