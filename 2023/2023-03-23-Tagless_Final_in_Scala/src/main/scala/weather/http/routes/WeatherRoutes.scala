package weather.http
package routes

import cats.implicits.*
import cats.effect.Sync
import org.http4s.*
import org.http4s.dsl.Http4sDsl
import org.http4s.dsl.impl.QueryParamDecoderMatcher
import org.http4s.server.Router
import org.http4s.dsl.io._
import org.http4s.circe._
import org.http4s.circe.CirceEntityEncoder.circeEntityEncoder

import weather.programs.WeatherProgramAlgebra
import weather.domain.City
import weather.domain.Weather


object CityQueryParamMatcher extends QueryParamDecoderMatcher[String]("city")

final case class WeatherRoutes[F[_]: Sync](
  weather: WeatherProgramAlgebra[F]
) extends Http4sDsl[F] {

  private[routes] val prefixPath = "/currentWeather"

  private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root :? CityQueryParamMatcher(city) =>
      for {
        eitherWeather <- weather.getCityWeather(City(city))
        resp <- eitherWeather match {
          case Left(err) => Ok("No data right now")
          case Right(weather) => Ok(weather)
        }
      } yield resp
  }

  val routes: HttpRoutes[F] = Router(
    prefixPath -> httpRoutes
  )
}