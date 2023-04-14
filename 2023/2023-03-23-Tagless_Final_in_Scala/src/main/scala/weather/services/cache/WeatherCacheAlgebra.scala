package weather.services.cache

import weather.domain.Weather

trait WeatherCacheAlgebra[F[_]] {
  def set(key: String, weather: Weather): F[Unit]
  def get(key: String): F[Option[Weather]]
}
  
