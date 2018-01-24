package controllers

import javax.inject._

import play.api.mvc._
import state.State

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class HomeController @Inject()(cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def index() = Action.async { implicit request: Request[AnyContent] =>
    val stateInActionBody = State.get
    println(s"HomeController.index: state is $stateInActionBody")
    Future {
      val stateInFutureBody = State.get
      println(s"HomeController.index.Future.apply: state is $stateInFutureBody")
      Ok(s"Action body: $stateInActionBody, future body: $stateInFutureBody")
    }
  }
}
