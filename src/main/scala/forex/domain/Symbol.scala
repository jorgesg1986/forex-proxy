package forex.domain

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class Symbol(currencies: String) {
  def toPair: Rate.Pair = {
    val ccy1 = currencies.substring(0, 3)
    val ccy2 = currencies.substring(3)

    Rate.Pair(Currency.fromString(ccy1), Currency.fromString(ccy2))
  }
}

object Symbol {

  implicit val encoder: Encoder[Symbol] = deriveEncoder[Symbol]

  implicit val decoder: Decoder[Symbol] = deriveDecoder[Symbol]

}