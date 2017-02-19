package cz.alenkacz.db.postgresscala

import scala.concurrent.Future

trait Connection {
    def query[T](query: String, deserializer: Row => T): Future[Seq[T]]
    def queryValue[T](query: String): Future[Option[T]]
    def execute[T](query: String): Future[Unit]
    def sendPreparedStatementForValue[T](query: String, values: Seq[Any] = Seq.empty): Future[Option[T]]
    def sendPreparedStatement[T](query: String, values: Seq[Any], deserializer: Row => T): Future[Seq[T]]
    def sendPreparedStatement[T](query: String, deserializer: Row => T): Future[Seq[T]]
}
