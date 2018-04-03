package forex.services.oneforge

import java.util.concurrent.TimeUnit

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import cats.Eval
import com.typesafe.scalalogging.LazyLogging
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import forex.config.{ApplicationConfig, ServiceConfig}
import forex.domain._
import forex.main.ActorSystems
import forex.services.oneforge.Error._
import org.zalando.grafter.macros.readerOf
import org.zalando.grafter.{Start, StartResult}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration

@readerOf[ApplicationConfig]
case class OneForgeService(serviceConf: ServiceConfig, actorSystem: ActorSystems) extends LazyLogging
  with FailFastCirceSupport with Start {

  import actorSystem.{materializer, system}

  override def start: Eval[StartResult] = StartResult.eval("OneForgeService")(())

  def getQuotes: Future[Either[Error, List[Rate]]] = {

    val uri = Uri(serviceConf.quotesUri)

    val query = Query("api_key" -> serviceConf.apiKey)

    val request = HttpRequest(uri = uri.withQuery(query))

    Http()
      .singleRequest(request)
      .flatMap{ response =>
        response.status match {
          case StatusCodes.OK =>
            val result: Future[Right[Nothing, List[Rate]]] = response.entity.toStrict(FiniteDuration(3, TimeUnit.SECONDS))
              .flatMap{entity =>

                logger.debug(s"Parsing response for strict quotes: ${entity}")

                Unmarshal(entity)
                  .to[List[Quote]]
                  .map(q => Right(q.map(_.toRate)))
              }

            result.map(r => logger.debug(s"Parsed quotes: ${r.value}"))
            result
          case code =>
            logger.warn(s"Issue while calling 1Forge to retrieve rates: ${code}")
            Future.successful(Left(ServiceCall(serviceConf.symbolsUri, code.intValue())))
        }
      }
  }

}
