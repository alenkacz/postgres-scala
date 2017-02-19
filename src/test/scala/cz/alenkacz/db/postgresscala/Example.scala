package cz.alenkacz.db.postgresscala

import java.util.concurrent.TimeUnit

import com.typesafe.config.ConfigFactory
import cz.alenkacz.db.postgresscala._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

object Example {
  def main(agrs: Array[String]): Unit = {
    implicit val connection: Connection = Await.result(PostgresConnection.fromConfig(ConfigFactory.load), Duration(3, TimeUnit.SECONDS))

    connection.query("SELECT 0", row => DomainObject(row(0).string, row(1).int))
    connection.queryValue[Long]("SELECT 0")
    connection.sendPreparedStatement("SELECT 0", row => DomainObject(row(0).string, row(1).int))
    connection.sendPreparedStatementForValue[Long]("SELECT 0")

    val testListValue = List("a", "b")
    sql"SELECT * FROM table WHERE a IN ($testListValue)".query(row => DomainObject(row(0).string, row(1).int))
    sql"SELECT id FROM table WHERE a IN ($testListValue)".queryValue[Int]
    val testValue = 1
    sql"SELECT * FROM table WHERE b=$testValue".query(row => DomainObject(row("a").string, row("b").int))
  }
}

case class DomainObject(testString: String, testInt: Int)