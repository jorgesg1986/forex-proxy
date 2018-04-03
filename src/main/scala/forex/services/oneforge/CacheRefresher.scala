package forex.services.oneforge

import java.util.concurrent.TimeUnit

import cats.Eval
import com.typesafe.scalalogging.LazyLogging
import forex.config.{ApplicationConfig, RefreshingConfig}
import forex.domain.Rate
import forex.main.ActorSystems
import org.zalando.grafter.macros.readerOf
import org.zalando.grafter.{Start, StartResult}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration
import scala.util.{Failure, Success}

@readerOf[ApplicationConfig]
case class CacheRefresher(config: RefreshingConfig, rateCache: RateCache, serviceAccess: OneForgeService, actorSystem: ActorSystems)
  extends LazyLogging with Start {

  override def start: Eval[StartResult] = {
    StartResult.eval("CacheRefresher") {

      actorSystem.system.scheduler.schedule(FiniteDuration(0, TimeUnit.SECONDS),
        FiniteDuration(config.refreshInterval, TimeUnit.SECONDS)) {
        refreshQuotes
      }
    }
  }

  def refreshQuotes: Future[Either[Error, Unit]] = {
    for {
      quotes : Either[Error, List[Rate]] <- serviceAccess.getQuotes
    } yield {
      quotes.map { q =>
        rateCache.put(q) match {
          case Success(_) => logger.debug("Success refreshing the rates")
          case Failure(err) => logger.warn(s"Error while refreshing rates cache: ${err.getMessage}")
        }
      }
    }
  }

}
