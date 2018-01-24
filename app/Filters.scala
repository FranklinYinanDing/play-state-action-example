import javax.inject.Inject

import play.api.http._
import state.StateFilter

class Filters @Inject()(stateFilter: StateFilter)
    extends DefaultHttpFilters(stateFilter) {
  println("Filters constructor: called")
}
