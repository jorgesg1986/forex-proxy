package forex.services.oneforge

import forex.config.CacheConfig
import forex.domain.Currency.{EUR, GBP, USD}
import forex.domain.{Price, Rate, Timestamp}
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

class RateCacheTest extends FlatSpec with Matchers with BeforeAndAfterAll {

  val cache = RateCache(CacheConfig(2))

  val pairEurGbp = Rate.Pair(EUR, GBP)
  val pairEurUsd = Rate.Pair(EUR, USD)

  val rateEurGbp = Rate(pairEurGbp, Price(1.5), Timestamp.now)
  val rateEurUsd = Rate(pairEurUsd, Price(1.5), Timestamp.now)

  val rates = List(
    rateEurGbp,
    rateEurUsd
  )

  override def beforeAll(): Unit = {
    cache.start.value
  }

  "RateCache" should "correctly load a list of Rates" in {

    cache.put(rates)

    assert(cache.get(pairEurGbp) == Right(rateEurGbp) &&
      cache.get(pairEurUsd) == Right(rateEurUsd) )
  }

  "RateCache" should "correctly expire a rate after the expiration" in {

    cache.put(rates)

    Thread.sleep(2000L)

    assert(cache.get(pairEurGbp) == Left(Error.PairNotFound))
  }

}
