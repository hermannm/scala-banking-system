object Task2 extends App {
  def initializeThread(function: => Unit): Thread = {
    return new Thread {
      override def run() = function;
    };
  }

  val thread = initializeThread {
    println("New thread initialized")
  }

  thread.start()
  thread.join()
  println("New thread joined");
}