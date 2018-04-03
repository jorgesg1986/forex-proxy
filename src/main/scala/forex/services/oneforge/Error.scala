package forex.services.oneforge

import scala.util.control.NoStackTrace

sealed trait Error extends NoStackTrace
object Error {
  final case object Generic extends Error
  final case class System(underlying: Throwable) extends Error
  final case object PairNotFound extends Error
  final case class ServiceCall(uri: String, statusCode: Int) extends Error
  final case class Parsing(underlying: Throwable) extends Error
}
