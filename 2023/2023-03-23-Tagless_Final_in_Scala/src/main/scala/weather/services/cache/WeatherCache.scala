package weather.services.cache

import java.time.Instant

import cats.syntax.all._
import cats.effect.Sync
import cats.effect.kernel.Ref

import weather.domain.Weather
import weather.domain.City

object WeatherCache {
  def make[F[_]: Sync](
    inMemoryStore: Ref[F, Map[String, (Long, Weather)]]
  ): WeatherCacheAlgebra[F] =
    new WeatherCacheAlgebra[F] {
      override def set(key: String, weather: Weather): F[Unit] = {
        val currentTimestamp = Instant.now.getEpochSecond
        inMemoryStore.update(state => state + (key -> (currentTimestamp, weather)))
      }
        
      override def get(key: String): F[Option[Weather]] =
        val currentTimestamp = Instant.now.getEpochSecond
        inMemoryStore.get.map(_.get(key).flatMap { case (cacheTime, weather) => 
          if (currentTimestamp - cacheTime > 10)
            None
          else
            Some(weather)
        })
    }
}
  
