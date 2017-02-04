package cz.alenkacz.db.postgresscala

import com.github.mauricio.async.db.pool.ConnectionPool
import com.github.mauricio.async.db.postgresql.PostgreSQLConnection
import com.github.mauricio.async.db.postgresql.pool.PostgreSQLConnectionFactory
import com.typesafe.config.{Config, ConfigFactory}
import cz.alenkacz.db.postgresscala.ConfigParser.PostgresConfiguration

object PostgresConnection {
  private val defaultConfig = ConfigFactory.defaultReference().getConfig("postgres-connection-defaults")
  def fromConfig(config: Config): Connection = {
    val finalConfig = config.withFallback(defaultConfig)

    val parsedConfig = ConfigParser.parse(finalConfig)
    parsedConfig match {
      case PostgresConfiguration(config, None) =>
        new PostgresAsyncConnection(new PostgreSQLConnection(config))
      case PostgresConfiguration(config, Some(poolConfig)) =>
        new PostgresAsyncConnection(new ConnectionPool[PostgreSQLConnection](new PostgreSQLConnectionFactory(config), poolConfig))
    }
  }
}