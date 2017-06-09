package cz.alenkacz.db.postgresscala
import com.github.mauricio.async.db.QueryResult
import com.github.mauricio.async.db.postgresql.exceptions.GenericDatabaseException
import com.github.mauricio.async.db.postgresql.messages.backend.{ErrorMessage, InformationMessage}

import scala.concurrent.{ExecutionContext, Future}

class PostgresAsyncConnection(underlyingConnection: com.github.mauricio.async.db.Connection)(implicit val executionContext: ExecutionContext) extends Connection {
  override def query[T](query: String, deserializer: (Row) => T): Future[Seq[T]] = sendQuery(query).map(qr => qr.rows match {
    case Some(resultSet) => resultSet.map(new PostgresAsyncRow(_)).map(deserializer)
    case None => Seq.empty
  })

  override def queryValue[T](query: String): Future[Option[T]] = sendQuery(query).map(qr => qr.rows.flatMap(r => r.headOption).map(r => r(0).asInstanceOf[T]))

  override def sendPreparedStatementForValue[T](query: String, values: Seq[Any]): Future[Option[T]] = sendPreparedStatement(query, values).map(qr => qr.rows.flatMap(r => r.headOption).map(r => r(0).asInstanceOf[T]))

  override def sendPreparedStatement[T](query: String, values: Seq[Any], deserializer: (Row) => T): Future[Seq[T]] = sendPreparedStatement(query, values).map(qr => qr.rows match {
    case Some(resultSet) => resultSet.map(new PostgresAsyncRow(_)).map(deserializer)
    case None => Seq.empty
  })

  override def sendPreparedStatement[T](query: String, deserializer: (Row) => T): Future[Seq[T]] = sendPreparedStatement(query, List()).map(qr => qr.rows match {
    case Some(resultSet) => resultSet.map(new PostgresAsyncRow(_)).map(deserializer)
    case None => Seq.empty
  })

  override def execute[T](query: String, values: Seq[Any] = Seq.empty): Future[Unit] = sendPreparedStatement(query, values).map(_ => ())

  private def sendPreparedStatement(query: String, values: Seq[Any]): Future[QueryResult] = underlyingConnection.sendPreparedStatement(query, values).recoverWith(handleExceptions)
  private def sendQuery(query: String): Future[QueryResult] = underlyingConnection.sendQuery(query).recoverWith(handleExceptions)

  private def isDuplicateKeyException(errorMessage: ErrorMessage): Boolean = errorMessage.fields.getOrElse(InformationMessage.SQLState, "") == "23505"

  private def handleExceptions: PartialFunction[Throwable, Future[QueryResult]] = {
    case e: GenericDatabaseException if isDuplicateKeyException(e.errorMessage) =>
      throw new DuplicateKeyException(e.errorMessage.fields.getOrElse(InformationMessage.Message, ""), e)
  }

  override def close(): Unit = underlyingConnection.disconnect
}
