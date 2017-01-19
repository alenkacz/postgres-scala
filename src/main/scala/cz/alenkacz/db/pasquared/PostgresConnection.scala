package cz.alenkacz.db.pasquared

import com.github.mauricio.async.db.Connection
import com.github.mauricio.async.db.pool.{ConnectionPool, PoolConfiguration}
import com.github.mauricio.async.db.postgresql.PostgreSQLConnection
import com.github.mauricio.async.db.postgresql.pool.PostgreSQLConnectionFactory
import com.typesafe.config.{Config, ConfigFactory}
import cz.alenkacz.db.pasquared.ConfigParser.PostgresConfiguration

import scala.util.Try

object PostgresConnection {
  val defaultConfig = ConfigFactory.defaultReference().getConfig("postgres-connection-defaults")
  def fromConfig(config: Config): Try[Connection] = {
    val finalConfig = config.withFallback(defaultConfig)

    ConfigParser.parse(finalConfig).map(c => c match {
      case PostgresConfiguration(config, None) => new PostgreSQLConnection(config)
      case PostgresConfiguration(config, Some(poolConfig)) =>
        new ConnectionPool[PostgreSQLConnection](new PostgreSQLConnectionFactory(config), poolConfig)
    })
  }
}