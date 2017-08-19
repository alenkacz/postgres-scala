package cz.alenkacz.db.postgresscala

import java.util.concurrent.TimeUnit

import com.github.mauricio.async.db.Configuration
import com.github.mauricio.async.db.pool.PoolConfiguration
import com.typesafe.config.ConfigFactory
import cz.alenkacz.db.postgresscala.ConfigParser.PostgresConfiguration
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.duration._
import scala.concurrent.duration.Duration

@RunWith(classOf[JUnitRunner])
class ConfigParserTest extends FlatSpec with Matchers {
  private val config = ConfigFactory.load("parser-test.conf")

  "Postgres configuration" should "be loaded from connection string" in {
    val actual = ConfigParser.parse(config.getConfig("connection-string"))
    val expected = PostgresConfiguration(
      Configuration("admin", "localhost", 5432, Some("admin"), Some("test_db")),
      Duration(5, TimeUnit.SECONDS),
      None)

    actual should be(expected)
  }

  it should "throw exception when connection string is missing" in {
    assertThrows[InvalidConfigurationException](
      ConfigParser.parse(config.getConfig("missing-connection-string")))
  }

  it should "parse full config" in {
    val actual = ConfigParser.parse(config.getConfig("full-config"))
    val expected = PostgresConfiguration(
      Configuration(
        "admin",
        "localhost",
        5432,
        Some("admin"),
        Some("test_db"),
        maximumMessageSize = 167,
        connectTimeout = 888 seconds,
        testTimeout = 777 seconds,
        queryTimeout = Some(666 seconds)
      ),
      Duration(5, TimeUnit.SECONDS),
      Some(PoolConfiguration(3, 5000, 10))
    )

    actual should be(expected)
  }

  it should "not return pooled connection by default" in {
    val actual = ConfigParser.parse(config.getConfig("without-pooling"))

    actual.poolConfiguration should be(None)
  }
}
