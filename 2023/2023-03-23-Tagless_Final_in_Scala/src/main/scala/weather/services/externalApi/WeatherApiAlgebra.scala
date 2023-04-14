package weather.services.externalApi

import weather.domain.Weather
import weather.domain.City

trait WeatherApiAlgebra[F[_]] {
  def get(city: City): F[Either[String, Weather]]
}
  
