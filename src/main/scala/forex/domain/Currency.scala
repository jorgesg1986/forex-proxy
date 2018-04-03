package forex.domain

import cats.Show
import io.circe._

sealed trait Currency
object Currency {
  final case object AUD extends Currency
  final case object CAD extends Currency
  final case object CHF extends Currency
  final case object EUR extends Currency
  final case object GBP extends Currency
  final case object NZD extends Currency
  final case object JPY extends Currency
  final case object SGD extends Currency
  final case object USD extends Currency
  final case object SEK extends Currency
  final case object NOK extends Currency
  final case object MXN extends Currency
  final case object ZAR extends Currency
  final case object TRY extends Currency
  final case object CNH extends Currency
  final case object XAU extends Currency
  final case object XAG extends Currency

  implicit val show: Show[Currency] = Show.show {
    case AUD ⇒ "AUD"
    case CAD ⇒ "CAD"
    case CHF ⇒ "CHF"
    case EUR ⇒ "EUR"
    case GBP ⇒ "GBP"
    case NZD ⇒ "NZD"
    case JPY ⇒ "JPY"
    case SGD ⇒ "SGD"
    case USD ⇒ "USD"
    case SEK ⇒ "SEK"
    case NOK ⇒ "NOK"
    case MXN ⇒ "MXN"
    case ZAR ⇒ "ZAR"
    case TRY ⇒ "TRY"
    case CNH ⇒ "CNH"
    case XAU ⇒ "XAU"
    case XAG ⇒ "XAG"
  }

  def fromString(s: String): Currency = s match {
    case "AUD" | "aud" ⇒ AUD
    case "CAD" | "cad" ⇒ CAD
    case "CHF" | "chf" ⇒ CHF
    case "EUR" | "eur" ⇒ EUR
    case "GBP" | "gbp" ⇒ GBP
    case "NZD" | "nzd" ⇒ NZD
    case "JPY" | "jpy" ⇒ JPY
    case "SGD" | "sgd" ⇒ SGD
    case "USD" | "usd" ⇒ USD
    case "SEK" | "sek" ⇒ SEK
    case "NOK" | "nok" ⇒ NOK
    case "MXN" | "mxn" ⇒ MXN
    case "ZAR" | "zar" ⇒ ZAR
    case "TRY" | "try" ⇒ TRY
    case "CNH" | "cnh" ⇒ CNH
    case "XAU" | "xau" ⇒ XAU
    case "XAG" | "xag" ⇒ XAG
  }

  implicit val encoder: Encoder[Currency] =
    Encoder.instance[Currency] { show.show _ andThen Json.fromString }

}
