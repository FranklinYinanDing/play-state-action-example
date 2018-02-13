import play.api.ApplicationLoader
import play.api.inject._
import play.api.inject.guice.{GuiceApplicationLoader, GuiceableModule}
import play.api.mvc._
import state.MyDefaultActionBuilderImpl

class CustomApplicationLoader extends GuiceApplicationLoader() {
  println("CustomApplicationLoader constructor: called")
  override protected def overrides(context: ApplicationLoader.Context): Seq[GuiceableModule] = {
    println("CustomApplicationLoader.overrides: called")
    Seq(bind[DefaultActionBuilder].to[MyDefaultActionBuilderImpl])
  }
}
