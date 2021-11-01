object Task2d extends App{
  def initializeThread(function: => Unit): Thread = {
    return new Thread {
      override def run() = function
    }
  }
  def print_deadlock(): Unit = println(deadlock_trap)

  lazy val deadlock_trap: String = {
    val worker_thread = initializeThread(print_deadlock)
    worker_thread.start()
    worker_thread.join()
    "deadlock"
  }
  print_deadlock()
}
