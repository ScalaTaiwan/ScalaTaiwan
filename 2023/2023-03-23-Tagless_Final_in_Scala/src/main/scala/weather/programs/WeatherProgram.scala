package weather.programs

import cats._
import cats.implicits._
import cats.data.EitherT
import cats.effect._
import org.typelevel.log4cats.Logger
import weather.domain.{City, Weather}
import weather.services.cache.WeatherCacheAlgebra
import weather.services.externalApi.WeatherApiAlgebra

object WeatherProgram {
  def make[F[_]: Monad : Logger](
    cacheService: WeatherCacheAlgebra[F],
    apiService: WeatherApiAlgebra[F]
  ): WeatherProgramAlgebra[F] = new WeatherProgramAlgebra[F] {
    
    override def getCityWeather(city: City): F[Either[String, Weather]] = for {
      cacseResult <- cacheService.get(city.name)
      result <- cacseResult match {
        case None => for {
          _ <- Logger[F].info(s"Cache not hit for key: ${city.name}")
          apiResult <- apiService.get(city)
          _ <- apiResult.foldMapM(weather => cacheService.set(city.name, weather))
        } yield apiResult
        case Some(weather) => 
          for {
            _ <- Logger[F].info(s"Get value in cache: ${city.name}")
            result <- EitherT.pure[F, String](weather).value
          } yield result
          
      }
    } yield result

  }
}

