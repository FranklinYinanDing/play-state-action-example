package state

import javax.inject.Inject

import akka.util.ByteString
import play.api.libs.streams.Accumulator
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

trait MyActionBuilder[+R[_], B] extends ActionBuilder[R, B] {
  /**
    * Compose the action with other actions.  This allows mixing in of various actions together.
    *
    * @param action The action to compose
    * @return The composed action
    */
  override protected def composeAction[A](action: Action[A]): Action[A] = new Action[A] {
    override def apply(request: Request[A]): Future[Result] = action.apply(request)

    override def parser: BodyParser[A] = action.parser

    override def executionContext: ExecutionContext = action.executionContext

    override def apply(rh: RequestHeader): Accumulator[ByteString, Result] = parser(rh).mapFuture {
      case Left(r) =>
        Future.successful(r)
      case Right(a) =>
        val request = Request(rh, a)
        apply(request)
    }(executionContext.prepare())
  }
}

class MyActionBuilderImpl[B](val parser: BodyParser[B])(implicit val executionContext: ExecutionContext)
  extends MyActionBuilder[Request, B] {
  def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = block(request)
}

class MyDefaultBodyParsers @Inject()(parser: BodyParsers.Default) extends BodyParser[AnyContent] {
  override def apply(rh: RequestHeader): Accumulator[ByteString, Either[Result, AnyContent]] = Accumulator(parser(rh).toSink)
}

class MyDefaultActionBuilderImpl(parser: BodyParser[AnyContent])(implicit ec: ExecutionContext)
  extends MyActionBuilderImpl(parser) with DefaultActionBuilder {
  println("StateActionBuilder constructor: called")
  @Inject
  def this(parser: MyDefaultBodyParsers)(implicit ec: ExecutionContext) = this(parser: BodyParser[AnyContent])
}
