object Task1 extends App {
  val array: Array[Int] = new Array[Int](50);

  for(i <- 0 to 49) {
    array(i) = i + 1;
  }

  println(array);
}