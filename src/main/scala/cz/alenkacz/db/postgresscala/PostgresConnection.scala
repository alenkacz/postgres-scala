package cz.alenkacz.db.postgresscala

import com.github.mauricio.async.db.pool.ConnectionPool
import com.github.mauricio.async.db.postgresql.PostgreSQLConnection
import com.github.mauricio.async.db.postgresql.pool.PostgreSQLConnectionFactory
import com.typesafe.config.{Config, ConfigFactory}
import cz.alenkacz.db.postgresscala.ConfigParser.PostgresConfiguration

import scala.concurrent.{ExecutionContext, Future}

object PostgresConnection {
  private val defaultConfig = ConfigFactory.defaultReference().getConfig("postgres-connection-defaults")
  def fromConfig(config: Config)(implicit executionContext: ExecutionContext): Future[Connection] = {
    val finalConfig = config.withFallback(defaultConfig)

    val parsedConfig = ConfigParser.parse(finalConfig)
    parsedConfig match {
      case PostgresConfiguration(conf, disconnectTimeout, None) =>
        new PostgreSQLConnection(conf).connect.map(new PostgresAsyncConnection(_, disconnectTimeout))
      case PostgresConfiguration(conf, disconnectTimeout, Some(poolConfig)) =>
        new ConnectionPool[PostgreSQLConnection](new PostgreSQLConnectionFactory(conf), poolConfig).connect.map(new PostgresAsyncConnection(_, disconnectTimeout))
    }
  }
}