import javax.inject.Inject

import play.api.http._
import state._

class Filters @Inject()(stateFilter: StateFilter, futureFilter: FutureFilter)
    extends DefaultHttpFilters(stateFilter, futureFilter) {
  println("Filters constructor: called")
}
