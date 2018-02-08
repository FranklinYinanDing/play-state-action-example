package state

import javax.inject.Inject

import play.api.libs.streams.Accumulator
import play.api.mvc._


import akka.util.ByteString

class SinkBodyParsers @Inject()(parser: BodyParsers.Default) extends BodyParser[AnyContent] {
  override def apply(rh: RequestHeader): Accumulator[ByteString, Either[Result, AnyContent]] = Accumulator(parser(rh).toSink)
}
