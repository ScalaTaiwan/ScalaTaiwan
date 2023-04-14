package weather.programs

import weather.domain.Weather
import weather.domain.City

trait WeatherProgramAlgebra[F[_]] {
  def getCityWeather(city: City): F[Either[String, Weather]]
}
