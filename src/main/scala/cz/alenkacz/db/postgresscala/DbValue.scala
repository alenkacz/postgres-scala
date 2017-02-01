package cz.alenkacz.db.postgresscala

class DbValue(value: Any) {
  def string(): String = value.asInstanceOf[String]
  def int(): Int = value.asInstanceOf[Int]
}
