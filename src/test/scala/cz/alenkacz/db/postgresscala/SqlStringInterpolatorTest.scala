package cz.alenkacz.db.postgresscala

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FlatSpec, Matchers}
import cz.alenkacz.db.postgresscala.Implicits._

@RunWith(classOf[JUnitRunner])
class SqlStringInterpolatorTest extends FlatSpec with Matchers  {
  "Interpolated sql string" should "be correcly parsed" in {
    val a = "value"
    val actual = sql"SELECT * FROM table WHERE a=$value"

    actual should be(new SqlQuery("SELECT * FROM table WHERE a=?", Seq(value)))
  }

  it should "interpolate also queries with list as parameter" in {
    val value = List("a", "b")
    val actual = sql"SELECT * FROM table WHERE a IN ($value)"

    actual should be(new SqlQuery("SELECT * FROM table WHERE a IN (?, ?)", Seq("a", "b")))
  }
}
