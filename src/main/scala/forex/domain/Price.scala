package forex.domain

import io.circe._
import io.circe.generic.semiauto._
import io.circe.generic.extras.wrapped._

case class Price(value: BigDecimal) extends AnyVal
object Price {
  def apply(value: Integer): Price =
    Price(BigDecimal(value))

  implicit val encoder: Encoder[Price] = deriveUnwrappedEncoder[Price]

  implicit val decoder: Decoder[Price] = deriveUnwrappedDecoder[Price]
}
