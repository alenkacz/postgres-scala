package cz.alenkacz.db.postgresscala

import scala.concurrent.Future

trait Connection {
    def isConnected: Boolean

    def sendQuery[T](query: String, deserializer: Row => T): Future[Seq[T]]
    def sendQuery[T](query: String): Future[Option[T]]
    def sendPreparedStatement[T](query: String, values: Seq[Any] = List()): Future[Option[T]]
    def sendPreparedStatement[T](query: String, values: Seq[Any], deserializer: Row => T): Future[Seq[T]]
    def sendPreparedStatement[T](query: String, deserializer: Row => T): Future[Seq[T]]
}
