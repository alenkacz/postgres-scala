package cz.alenkacz.db.postgresscala

object Example {
  def main(agrs: Array[String]): Unit = {
    val connection: Connection = ???

    connection.query("SELECT 0", row => DomainObject(row(0).string(), row(1).int()))
    connection.queryValue[Long]("SELECT 0")
    connection.sendPreparedStatement("SELECT 0", row => DomainObject(row(0).string(), row(1).int()))
    connection.sendPreparedStatementForValue[Long]("SELECT 0")
  }
}

case class DomainObject(testString: String, testInt: Int)