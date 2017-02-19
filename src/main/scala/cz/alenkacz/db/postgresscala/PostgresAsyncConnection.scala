package cz.alenkacz.db.postgresscala
import scala.concurrent.{ExecutionContext, Future}

class PostgresAsyncConnection(underlyingConnection: com.github.mauricio.async.db.Connection)(implicit val executionContext: ExecutionContext) extends Connection {
  override def query[T](query: String, deserializer: (Row) => T): Future[Seq[T]] = underlyingConnection.sendQuery(query).map(qr => qr.rows match {
    case Some(resultSet) => resultSet.map(new PostgresAsyncRow(_)).map(deserializer)
    case None => Seq.empty
  })

  override def queryValue[T](query: String): Future[Option[T]] = underlyingConnection.sendQuery(query).map(qr => qr.rows.flatMap(r => r.headOption).map(r => r(0).asInstanceOf[T]))

  override def sendPreparedStatementForValue[T](query: String, values: Seq[Any]): Future[Option[T]] = underlyingConnection.sendPreparedStatement(query, values).map(qr => qr.rows.flatMap(r => r.headOption).map(r => r(0).asInstanceOf[T]))

  override def sendPreparedStatement[T](query: String, values: Seq[Any], deserializer: (Row) => T): Future[Seq[T]] = underlyingConnection.sendPreparedStatement(query, values).map(qr => qr.rows match {
    case Some(resultSet) => resultSet.map(new PostgresAsyncRow(_)).map(deserializer)
    case None => Seq.empty
  })

  override def sendPreparedStatement[T](query: String, deserializer: (Row) => T): Future[Seq[T]] = underlyingConnection.sendPreparedStatement(query, List()).map(qr => qr.rows match {
    case Some(resultSet) => resultSet.map(new PostgresAsyncRow(_)).map(deserializer)
    case None => Seq.empty
  })

  override def execute[T](query: String): Future[Unit] = underlyingConnection.sendQuery(query).map(_ => ())
}
