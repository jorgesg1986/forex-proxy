package forex.services.oneforge

import java.util.concurrent.TimeUnit

import cats.Eval
import com.google.common.cache.{Cache, CacheBuilder}
import forex.config.{ApplicationConfig, CacheConfig}
import forex.domain.Rate
import org.zalando.grafter.macros.readerOf
import org.zalando.grafter.{Start, StartResult}

import scala.util.Try

/**
  * Object to store the rates for all available currencies upon request
  * The rates will self expire according to the configuration
  */

@readerOf[ApplicationConfig]
case class RateCache(conf: CacheConfig) extends Start {

  private var rateCache: Cache[Rate.Pair, Rate] = _

  override def start: Eval[StartResult] = StartResult.eval("RateCache"){
    rateCache = CacheBuilder.newBuilder()
      .expireAfterWrite(conf.ccyExpiration, TimeUnit.SECONDS)
      .build[Rate.Pair, Rate]()
  }

  def get(pair: Rate.Pair): Either[Error, Rate] = {
    rateCache.getIfPresent(pair) match {
      case null => Left(Error.PairNotFound)
      case rate: Rate => Right(rate)
    }
  }

  def put(rates: List[Rate]): Try[Unit] = {
    Try(rates.foreach(rate => rateCache.put(rate.pair, rate)))
  }

}
