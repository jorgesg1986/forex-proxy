package forex.processes.rates

import forex.domain._
import scala.util.control.NoStackTrace

package object messages {
  sealed trait Error extends NoStackTrace
  object Error {
    final case object Generic extends Error
    final case class System(underlying: Throwable) extends Error
    final case object PairNotFound extends Error
    final case class ServiceCall(uri: String, statusCode: Int) extends Error
  }

  final case class GetRequest(
      from: Currency,
      to: Currency
  )
}
