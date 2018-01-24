package state

import java.util.concurrent.TimeUnit

import akka.dispatch.{Dispatcher, DispatcherPrerequisites, MessageDispatcher, MessageDispatcherConfigurator}
import com.typesafe.config.Config

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.Duration

class StateDispatcherConfigurator(config: Config, prerequisites: DispatcherPrerequisites) extends MessageDispatcherConfigurator(config, prerequisites) {
  println("StateDispatcherConfigurator constructor: called")

  private val instance = new Dispatcher(
    this,
    config.getString("id"),
    config.getInt("throughput"),
    Duration(config.getDuration("throughput-deadline-time", TimeUnit.NANOSECONDS), TimeUnit.NANOSECONDS),
    configureExecutor(),
    Duration(config.getDuration("shutdown-timeout", TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS)) { parentDispatcher =>

    override def prepare(): ExecutionContext = {
      new ExecutionContext {
        private val preparedState = State.get
        println(s"StateDispatcherConfigurator.instance.prepare: preparing with state $preparedState")
        override def execute(runnable: Runnable): Unit = parentDispatcher.execute(() => {
          State.set(preparedState, "StateDispatcherConfigurator.instance.prepare.execute") { runnable.run() }
        })
        override def reportFailure(cause: Throwable): Unit = parentDispatcher.reportFailure(cause)
        override def toString: String = s"StateDispatcherConfigurator.instance:prepared($preparedState)"
      }
    }
    override val toString: String = s"StateDispatcherConfigurator.instance:unprepared"
  }
  override def dispatcher(): MessageDispatcher = instance

}