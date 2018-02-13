package state

import javax.inject.Inject

import akka.actor.ActorSystem
import akka.stream.Materializer
import play.api.mvc.{Filter, RequestHeader, Result}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext

class FutureFilter @Inject() (actorSystem: ActorSystem)(implicit override val mat: Materializer, ec: ExecutionContext) extends Filter {
  override def apply(next: RequestHeader => Future[Result])(rh: RequestHeader): Future[Result] = {
    Future(next(rh)).flatten
  }
}
