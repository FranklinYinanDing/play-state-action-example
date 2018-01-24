package state

import java.util.concurrent.ThreadLocalRandom

object State {
  def randomState: Int = {
    val r = ThreadLocalRandom.current.nextInt()
    println(s"State.random: new random state: $r")
    r
  }
  private val threadLocal = new ThreadLocal[Int]
  def get: Int = threadLocal.get
  def set[A](newState: Int, debugMessage: String)(body: => A): A = {
    val oldState = get
    println(s"$debugMessage: State.set: setting state from $oldState to $newState")
    threadLocal.set(newState)
    try body finally {
      println(s"$debugMessage: State.set: restoring state from $newState to $oldState")
      threadLocal.set(oldState)
    }
  }
}
