package forex.interfaces.api.utils

import akka.http.scaladsl._
import forex.processes._
import forex.processes.rates.messages._

object ApiExceptionHandler {

  def apply(): server.ExceptionHandler =
    server.ExceptionHandler {
      case Error.PairNotFound => ctx ⇒ ctx.complete("The pair of currencies hasn't been found in the internal cache")
      case Error.ServiceCall(uri, code) => ctx ⇒
        ctx.complete(s"Error $code while trying to retrieve rates from external service at $uri")
      case _: RatesError ⇒
        ctx ⇒
          ctx.complete("Something went wrong in the rates process")
      case _: Throwable ⇒
        ctx ⇒
          ctx.complete("Something else went wrong")
    }

}
