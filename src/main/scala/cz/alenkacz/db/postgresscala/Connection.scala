package cz.alenkacz.db.postgresscala

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.util.Failure

trait Connection extends AutoCloseable {
    def query[T](query: String, deserializer: Row => T): Future[Seq[T]]
    def queryValue[T](query: String): Future[Option[T]]
    def execute[T](query: String, values: Seq[Any] = Seq.empty): Future[Unit]
    def sendPreparedStatementForValue[T](query: String, values: Seq[Any] = Seq.empty): Future[Option[T]]
    def sendPreparedStatement[T](query: String, values: Seq[Any], deserializer: Row => T): Future[Seq[T]]
    def sendPreparedStatement[T](query: String, deserializer: Row => T): Future[Seq[T]]

    def inTransaction[A](f : Connection => Future[A])(implicit ec : ExecutionContext) : Future[A] = {
        execute("BEGIN").flatMap { _ =>
            val p = Promise[A]()
            f(this).onComplete { r =>
                execute(if (r.isFailure) "ROLLBACK" else "COMMIT").onComplete {
                    case Failure(e) if r.isSuccess => p.failure(e)
                    case _ => p.complete(r)
                }
            }
            p.future
        }
    }
}
