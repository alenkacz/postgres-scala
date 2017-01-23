# postgres-scala
Asynchronous postgres client for Scala

# Convenient builder
Since [typesafe config](https://github.com/typesafehub/config) is de facto standard configuration library for scala, there is an easy way how to create postgres client directly from config. To do that, you need to have a config similar to this one:
 
	database {
      connectionString = "jdbc:postgresql://localhost:5432/test_db"
    
      maxMessageSize = 16777216 // optional
      connectTimeout = 5 seconds // optional
      testTimeout = 5 seconds // optional
      queryTimeout = 6 seconds // optional
    
      // optional, do not use pool section if you do not want pooled connection
      pool {
        maxConnections = 3
        idleTime = 5 seconds
        maxQueueSize = 10
      }
    }

To create the connection in the code, all you have to do is call

	import cz.alenkacz.db.postgresscala
	import import com.typesafe.config.ConfigFactory
	
	val connection = PostgresConnection.fromConfig(ConfigFactory.load().getConfig("database"))