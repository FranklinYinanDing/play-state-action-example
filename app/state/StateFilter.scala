package state

import javax.inject.Inject

import akka.actor.ActorSystem
import akka.stream.Materializer
import play.api.mvc.{Filter, RequestHeader, Result}

import scala.concurrent.Future

class StateFilter @Inject() (actorSystem: ActorSystem)(implicit override val mat: Materializer) extends Filter {
  println(s"StateFilter constructor: actorSystem.dispatcher = ${actorSystem.dispatcher}")
  println(s"StateFilter constructor: mat.executionContext = ${mat.executionContext}")
  override def apply(next: RequestHeader => Future[Result])(rh: RequestHeader): Future[Result] = {
    val newState = State.randomState
    println(s"StateFilter.apply: initialising state to random state $newState")
    State.set(newState, "StateFilter.apply") {
      println("StateFilter.apply: calling next action/filter in chain")
      next(rh)
    }
  }
}
