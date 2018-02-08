package state

import javax.inject._

import play.api.mvc._

import scala.concurrent.ExecutionContext

@Singleton
class StateActionBuilder(parser: BodyParser[AnyContent])(implicit ec: ExecutionContext)
    extends ActionBuilderImpl(parser) with DefaultActionBuilder {
  println("StateActionBuilder constructor: called")
  @Inject
  def this(parser: SinkBodyParsers)(implicit ec: ExecutionContext) = this(parser: BodyParser[AnyContent])

  override protected def composeAction[A](originalAction: Action[A]): Action[A] = {
    println("StateActionBuilder.composeAction: called")
    new StateAction[A](originalAction)
  }
}
