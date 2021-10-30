object Task2 extends App {
  def initializeThread(function: => Unit): Thread = {
    return new Thread {
      override def run() = function
    }
  }

  private var counter: Int = 0

  def increaseCounter(): Unit = counter += 1

  def safeIncreaseCounter(): Unit = this.synchronized(increaseCounter)

  def printCounter(): Unit = println(counter)

  val thread1 = initializeThread(safeIncreaseCounter)
  val thread2 = initializeThread(safeIncreaseCounter)
  val thread3 = initializeThread(printCounter)

  thread1.start()
  thread2.start()
  thread3.start()
}