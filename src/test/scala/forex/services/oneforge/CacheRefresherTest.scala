package forex.services.oneforge

import forex.config.{AkkaConfig, CacheConfig, RefreshingConfig, ServiceConfig}
import forex.domain.Currency.{EUR, GBP, USD}
import forex.domain.{Price, Rate, Timestamp}
import forex.main.ActorSystems
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

import scala.concurrent.Future

class CacheRefresherTest extends FlatSpec with Matchers with BeforeAndAfterAll {

  val pairEurGbp = Rate.Pair(EUR, GBP)
  val pairEurUsd = Rate.Pair(EUR, USD)

  val rateEurGbp = Rate(pairEurGbp, Price(1.5), Timestamp.now)
  val rateEurUsd = Rate(pairEurUsd, Price(1.5), Timestamp.now)

  val rates = List(
    rateEurGbp,
    rateEurUsd
  )

  val systems = ActorSystems(AkkaConfig("test", None))
  val service = new OneForgeTest(ServiceConfig("", "", ""), systems)
  service.setResult(Right(rates))
  val cache = RateCache(CacheConfig(2))

  val refresher = CacheRefresher(RefreshingConfig(5), cache, service, systems)

  override def beforeAll(): Unit = {
    cache.start.value
    refresher.start.value
  }

  "CacheRefresher" should "initially load the rates in a cache automatically" in {
    Thread.sleep(1000L)
    assert(cache.get(pairEurGbp) == Right(rateEurGbp) &&
      cache.get(pairEurUsd) == Right(rateEurUsd) )
  }

  "CacheRefresher" should "refresh the rates in a cache automatically" in {
    Thread.sleep(3000L)
    assert(cache.get(pairEurGbp) == Left(Error.PairNotFound) &&
      cache.get(pairEurUsd) == Left(Error.PairNotFound) )

    Thread.sleep(2000L)

    assert(cache.get(pairEurGbp) == Right(rateEurGbp) &&
      cache.get(pairEurUsd) == Right(rateEurUsd) )
  }


}

class OneForgeTest(service: ServiceConfig, system: ActorSystems) extends OneForgeService(service, system) {

  var result: Either[Error, List[Rate]] = Right(List.empty[Rate])

  def setResult(newResult: Either[Error, List[Rate]]): Unit = result = newResult

  override def getQuotes: Future[Either[Error, List[Rate]]] = {
    Future.successful(result)
  }

}