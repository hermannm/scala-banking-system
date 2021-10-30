object Task1 extends App {
  val array: Array[Int] = new Array[Int](50);

  for(i <- 0 to 49) {
    array(i) = i + 1;
  }

  println(array);

  def fibonacci(n: Int): BigInt = {
    if (n <= 1)
      return n
    fibonacci(n-1) + fibonacci(n-2)
  }

  println(s"Fibonacci number 30 is ${fibonacci(30)}")
}
