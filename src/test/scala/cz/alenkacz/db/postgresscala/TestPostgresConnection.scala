package cz.alenkacz.db.postgresscala

import java.util.concurrent.TimeUnit

import com.typesafe.config.ConfigFactory

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

trait TestPostgresConnection {
  implicit lazy val connection: Connection = Await.result(PostgresConnection.fromConfig(ConfigFactory.load("integration-test.conf").getConfig("postgres")), Duration(3, TimeUnit.SECONDS))
}
