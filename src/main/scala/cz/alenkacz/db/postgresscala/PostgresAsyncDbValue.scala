package cz.alenkacz.db.postgresscala

class PostgresAsyncDbValue(value: Any) extends DbValue {
  override def string(): String = value.asInstanceOf[String]
  override def int(): Int = value.asInstanceOf[Int]
}
