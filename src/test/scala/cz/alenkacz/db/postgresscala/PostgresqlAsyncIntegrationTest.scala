package cz.alenkacz.db.postgresscala

import java.util.concurrent.TimeUnit

import org.scalatest.{AsyncFlatSpec, BeforeAndAfterAll, FlatSpec, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import cz.alenkacz.db.postgresscala._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PostgresqlAsyncIntegrationTest extends AsyncFlatSpec with Matchers with TestPostgresConnection {

  "Postgres scala" should "return result for basic query" in {
    createTestTable()
    val expected = TestObject("a", Seq("a", "b"))
    connection.execute("INSERT INTO abc (a, b) VALUES ('a', '{\"a\", \"b\"}')").flatMap(_ => sql"SELECT a,b FROM abc".query(r => TestObject(r(0).string, r(1).strings))).flatMap(actual => {
      actual.head should be(expected)
    })
  }

  def createTestTable() = {
    Await.result(connection.execute("""CREATE TABLE IF NOT EXISTS abc (
           id serial PRIMARY KEY,
           a text NOT NULL UNIQUE,
           b text[] NOT NULL
         );"""), Duration(3, TimeUnit.SECONDS))
  }
}

case class TestObject(testString: String, testStringArray: Seq[String])
