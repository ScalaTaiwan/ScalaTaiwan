package weather.services.externalApi

import cats.implicits._
import cats.effect._
import org.http4s._
import org.http4s.client._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.circe._
import io.circe._
import io.circe.generic.semiauto._
import weather.domain.City
import weather.domain.Weather
import weather.domain.Weather._


object WeatherApi{

  final case class WeatherApiResponse(current: Weather)
  implicit val weatherApiResponseDecoder: Decoder[WeatherApiResponse] = deriveDecoder

  def make[F[_]: Concurrent](client: Client[F]): WeatherApiAlgebra[F] = 

    implicit val weatherApiResponseEntityDecoder: EntityDecoder[F, WeatherApiResponse] = jsonOf[F, WeatherApiResponse]
    new WeatherApiAlgebra[F] {
      val weatherApiToken = "YOUR_TOKEN"
      val baseUri = uri"https://api.weatherapi.com"
      val uriWithPath = baseUri.withPath(path"/v1/current.json")
      def get(city: City): F[Either[String, Weather]] = {
        val uriWithPathAndQuery = 
          uriWithPath.withQueryParams(Map("q" -> city.name, "key" -> weatherApiToken))
        val request = Request[F](
          method = GET,
          uri = uriWithPathAndQuery
        )

        client.run(request).use {
          case Status.Successful(resp) => 
            resp.asJsonDecode[WeatherApiResponse].map(x => Right(x.current))
          case resp => 
            resp.as[String].map(msg => Left(msg))
        }
        
      }
    }
}
  
