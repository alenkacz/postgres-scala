# postgres-scala
---------------------

[![Build Status](https://travis-ci.org/alenkacz/postgres-scala.svg)](https://travis-ci.org/alenkacz/postgres-scala) [ ![Download](https://api.bintray.com/packages/alenkacz/maven/postgres-scala_2.12/images/download.svg) ](https://bintray.com/alenkacz/maven/postgres-scala_2.12/_latestVersion)

Asynchronous postgres client for Scala 2.12. It does not reimplement the wheel because currently it uses [postgresql-async](https://github.com/mauricio/postgresql-async) under the hood. So it is just a nicer API for this library.

## Motivation
There are several very good postgresql clients for Scala but in my opinion there is still room for another one for these reasons:
- [scalike](https://github.com/scalikejdbc/scalikejdbc) (asynchronous implementation is not in production-ready state)
- [postgresql-async](https://github.com/mauricio/postgresql-async) (the API is too low-level)
- [slick](https://github.com/slick/slick) (too heavy for plain SQL usage)

Sometimes your just want to write plain SQL queries (e.g. for performance reasons) and map them to domain objects by hand. This library enable that with a nice scala API.

## Getting started
Current version of the library can be found on [bintray](https://bintray.com/alenkacz/maven/postgres-scala_2.12/_latestVersion)

SBT
```groovy
"cz.alenkacz.db" %% "postgres-scala_2.12" % "<VERSION_HERE>"
```
Gradle
```groovy
compile 'cz.alenkacz.db:postgres-scala_2.12:<VERSION_HERE>'
```

### Example usage
```scala
package cz.alenkacz.db.postgresscala

import com.typesafe.config.ConfigFactory
import cz.alenkacz.db.postgresscala._

import scala.concurrent.ExecutionContext.Implicits.global

object Example {
	def main(agrs: Array[String]): Unit = {
		implicit val connection: Connection = PostgresConnection.fromConfig(ConfigFactory.load())

		val testListValue = List("a", "b")
		sql"SELECT * FROM table WHERE a IN ($testListValue)".query(row => DomainObject(row(0).string(), row(1).int()))
		sql"SELECT id FROM table WHERE a IN ($testListValue)".queryValue[Int]()
		val testValue = 1
		sql"SELECT * FROM table WHERE b=$testValue".query(row => DomainObject(row("a").string(), row("b").int()))
		connection.inTransaction { c =>
		  for {
		     val1 <- c.query("SELECT COUNT(*) FROM table1", r => r(0).int)
		     val2 <- c.query("SELECT COUNT(*) FROM table2", r => r(0).int)
		  } yield val1 + val2
		}
	}
}

case class DomainObject(testString: String, testInt: Int)
```

## Convenient builder
Since [typesafe config](https://github.com/typesafehub/config) is de facto standard configuration library for scala, there is an easy way how to create postgres client directly from config. To do that, you need to have a config similar to this one:
``` 
database {
  connectionString = "jdbc:postgresql://localhost:5432/test_db"

  maxMessageSize = 16777216 // optional
  connectTimeout = 5 seconds // optional
  disconnectTimeout = 5 seconds // optional
  testTimeout = 5 seconds // optional
  queryTimeout = 6 seconds // optional

  // optional, do not use pool section if you do not want pooled connection
  pool {
    maxConnections = 3
    idleTime = 5 seconds
    maxQueueSize = 10
  }
}
```

To create the connection in the code, all you have to do is call
```scala
	import cz.alenkacz.db.postgresscala
	import import com.typesafe.config.ConfigFactory
	
	val connection = PostgresConnection.fromConfig(ConfigFactory.load().getConfig("database"))
```
