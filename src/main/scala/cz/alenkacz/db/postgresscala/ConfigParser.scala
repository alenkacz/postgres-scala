package cz.alenkacz.db.postgresscala

import com.github.mauricio.async.db.Configuration
import com.github.mauricio.async.db.pool.PoolConfiguration
import com.github.mauricio.async.db.postgresql.util.URLParser
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.duration.Duration

object ConfigParser {
  private final val MaxMessageSize = "maxMessageSize"
  private final val ConnectTimeout = "connectTimeout"
  private final val DisconnectTimeout = "disconnectTimeout"
  private final val TestTimeout = "testTimeout"
  private final val QueryTimeout = "queryTimeout"
  private final val PoolConfig = "pool"
  private final val PoolMaxConnections = "maxConnections"
  private final val PoolIdleTime = "idleTime"
  private final val PoolMaxQueueSize = "maxQueueSize"
  private final val PoolValidationInterval = "validationInterval"

  private val mauricioDefaultValues = URLParser.DEFAULT
  private val fallbackConfig =
    ConfigFactory.defaultReference().getConfig("postgres-connection-defaults")

  def parse(config: Config): PostgresConfiguration = {
    PostgresConfiguration(
      getConfiguration(config),
      config.withFallback(fallbackConfig).getDuration(DisconnectTimeout),
      getPoolConfiguration(config))
  }

  implicit def asFiniteDuration(d: java.time.Duration): Duration =
    scala.concurrent.duration.Duration.fromNanos(d.toNanos)

  private def getPoolConfiguration(
      config: Config): Option[PoolConfiguration] = {
    if (config.hasPath(PoolConfig)) {
      val poolConfig = config.getConfig(PoolConfig)
      if (!poolConfig.hasPath(PoolMaxConnections) || !poolConfig.hasPath(
            PoolIdleTime) || !poolConfig.hasPath(PoolMaxQueueSize)) {
        throw new InvalidConfigurationException(
          s"fields $PoolMaxQueueSize, $PoolIdleTime and $PoolMaxConnections are mandatory to setup pooled postgresql connections",
          null)
      }
      Some(
        PoolConfiguration(poolConfig.getInt(PoolMaxConnections),
                          poolConfig.getDuration(PoolIdleTime).toMillis,
                          poolConfig.getInt(PoolMaxQueueSize)))
    } else {
      None
    }
  }

  private def getConfiguration(config: Config): Configuration = {
    val connectionStringConfig = connectionString(config)
    val maxMessageSizeConfig =
      if (config.hasPath(MaxMessageSize)) config.getInt(MaxMessageSize)
      else mauricioDefaultValues.maximumMessageSize
    val connectTimeoutConfig: Duration =
      if (config.hasPath(ConnectTimeout)) config.getDuration(ConnectTimeout)
      else mauricioDefaultValues.connectTimeout
    val testTimeoutConfig: Duration =
      if (config.hasPath(TestTimeout)) config.getDuration(TestTimeout)
      else mauricioDefaultValues.testTimeout
    val queryTimeoutConfig: Option[Duration] =
      if (config.hasPath(QueryTimeout)) Some(config.getDuration(QueryTimeout))
      else mauricioDefaultValues.queryTimeout

    connectionStringConfig.copy(maximumMessageSize = maxMessageSizeConfig,
                                connectTimeout = connectTimeoutConfig,
                                testTimeout = testTimeoutConfig,
                                queryTimeout = queryTimeoutConfig)
  }

  private def connectionString(config: Config): Configuration = {
    if (!config.hasPath("connectionString")) {
      throw new InvalidConfigurationException(
        "missing property connection string",
        null)
    } else {
      try {
        URLParser.parseOrDie(config.getString("connectionString"))
      } catch {
        case e: RuntimeException =>
          throw new InvalidConfigurationException(
            s"Incorrect format of the connection string, expected JDBC connection string, actual: ${config
              .getString("connectionString")}",
            e)
      }
    }
  }

  case class PostgresConfiguration(configuration: Configuration,
                                   disconnectTimeout: Duration,
                                   poolConfiguration: Option[PoolConfiguration])
}

class InvalidConfigurationException(message: String, exception: Throwable)
    extends RuntimeException(
      s"Unexpected format of the configuration: $message",
      exception)
