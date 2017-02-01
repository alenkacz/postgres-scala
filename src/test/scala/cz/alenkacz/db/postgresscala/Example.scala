package cz.alenkacz.db.postgresscala

object Example {
  def main(agrs: Array[String]): Unit = {
    val connection: Connection = ???

    connection.sendQuery("SELECT 0", row => DomainObject(row(0).string(), row(1).int()))
    connection.sendQuery[Long]("SELECT 0")
    connection.sendPreparedStatement("SELECT 0", row => DomainObject(row(0).string(), row(1).int()))
    connection.sendPreparedStatement[Long]("SELECT 0")
  }
}

case class DomainObject(testString: String, testInt: Int)