package forex.config

import org.zalando.grafter.macros._

import scala.concurrent.duration.FiniteDuration

@readers
case class ApplicationConfig(
    akka: AkkaConfig,
    api: ApiConfig,
    executors: ExecutorsConfig,
    service: ServiceConfig,
    cache: CacheConfig,
    refreshing: RefreshingConfig
)

case class AkkaConfig(
    name: String,
    exitJvmTimeout: Option[FiniteDuration]
)

case class ApiConfig(
    interface: String,
    port: Int
)

case class ExecutorsConfig(
    default: String
)

case class ServiceConfig(
    apiKey: String,
    quotesUri: String,
    symbolsUri: String
)

case class CacheConfig(
    ccyExpiration: Int
)

case class RefreshingConfig(
    refreshInterval: Int
)
