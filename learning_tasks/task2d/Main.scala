object Task2d extends App{
  lazy val deadlock_list: List[String] = List("deadlock", "in", "3", "2", "1", deadlocker)
  lazy val deadlocker: String = deadlock_list.head
  println(deadlocker)
}