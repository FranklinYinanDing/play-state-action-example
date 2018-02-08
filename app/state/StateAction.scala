package state

import akka.util.ByteString
import play.api.libs.streams.Accumulator
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class StateAction[A](originalAction: Action[A]) extends Action[A] {
  override def parser: BodyParser[A] = originalAction.parser
  override def apply(request: Request[A]): Future[Result] = originalAction.apply(request)
  override def executionContext: ExecutionContext = originalAction.executionContext
  override def apply(rh: RequestHeader): Accumulator[ByteString, Result] = {
    parser(rh).mapFuture {
      case Left(r) =>
        Future.successful(r)
      case Right(a) =>
        val request = Request[A](rh, a)
        apply(request)
    }(executionContext.prepare)
  }
}
