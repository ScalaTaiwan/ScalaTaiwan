object Sentinel extends Runnable {
  override def run(): Unit = {
    while (true) {
      println(s"Active threads count: ${Thread.activeCount}")
      Thread.sleep(3000)
    }
  }
}
