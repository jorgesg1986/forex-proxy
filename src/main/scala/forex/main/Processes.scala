package forex.main

import forex.config._
import forex.services.oneforge.RateCache
import forex.{services => s}
import forex.{processes => p}
import org.zalando.grafter.macros._

@readerOf[ApplicationConfig]
case class Processes(cache: RateCache) {

  implicit final lazy val _oneForge: s.OneForge[AppEffect] =
    s.OneForge.live(cache)

  final val Rates = p.Rates[AppEffect]

}
