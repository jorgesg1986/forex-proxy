package forex.domain

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto._
import java.time.{Instant, OffsetDateTime, ZoneId}

case class Quote(
                symbol: String,
                bid: Price,
                ask: Price,
                price: Price,
                timestamp: Long
) {

  def toPair: Rate.Pair = {
    val ccy1 = symbol.substring(0, 3)
    val ccy2 = symbol.substring(3)

    Rate.Pair(Currency.fromString(ccy1), Currency.fromString(ccy2))
  }

  def toRate: Rate = {
    Rate(toPair, price, Timestamp(OffsetDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault())))
  }
}

object Quote {

  implicit val encoder: Encoder[Quote] = deriveEncoder[Quote]

  implicit val decoder: Decoder[Quote] = deriveDecoder[Quote]

}
