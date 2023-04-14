package weather.domain

import io.circe._
import io.circe.generic.semiauto._

final case class Weather(
  humidity: Int,
  temp_c: Float
)

object Weather {

  implicit val weatherEncoder: Encoder[Weather] = deriveEncoder
  implicit val weatherDecoder: Decoder[Weather] = deriveDecoder

}