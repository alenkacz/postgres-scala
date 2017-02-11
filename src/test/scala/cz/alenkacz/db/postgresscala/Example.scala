package cz.alenkacz.db.postgresscala

import cz.alenkacz.db.postgresscala._

object Example {
  def main(agrs: Array[String]): Unit = {
    implicit val connection: Connection = ???

    connection.query("SELECT 0", row => DomainObject(row(0).string(), row(1).int()))
    connection.queryValue[Long]("SELECT 0")
    connection.sendPreparedStatement("SELECT 0", row => DomainObject(row(0).string(), row(1).int()))
    connection.sendPreparedStatementForValue[Long]("SELECT 0")

    val testValue = List("a", "b")
    sql"SELECT * FROM table WHERE a IN ($testValue)".query(row => DomainObject(row(0).string(), row(1).int()))
    sql"SELECT id FROM table WHERE a IN ($testValue)".queryValue[Int]()
  }
}

case class DomainObject(testString: String, testInt: Int)