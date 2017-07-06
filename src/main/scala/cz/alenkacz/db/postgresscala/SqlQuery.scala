package cz.alenkacz.db.postgresscala

import scala.concurrent.Future

case class SqlQuery(query: String, params: Seq[Any]) {
  def execute()(implicit connection: Connection): Future[Unit] = connection.execute(query, params)
  def query[T](deserializer: Row => T)(implicit connection: Connection): Future[Seq[T]] = connection.sendPreparedStatement(query, params, deserializer)
  def queryValue[T]()(implicit connection: Connection): Future[Option[T]] = connection.sendPreparedStatementForValue(query, params)
  def count()(implicit connection: Connection): Future[Long] = connection.count(query)
}
