package weather.programs

import cats.effect.IO
import org.http4s._
import org.http4s.implicits._
import munit.CatsEffectSuite
import org.typelevel.log4cats.SelfAwareStructuredLogger
import org.typelevel.log4cats.noop.NoOpLogger
import weather.domain.Weather
import weather.domain.City
import weather.services.cache.WeatherCacheAlgebra
import weather.services.externalApi.WeatherApiAlgebra
import weather.programs.WeatherProgram

class WeatherProgramSpec extends CatsEffectSuite {
  
  given logger: SelfAwareStructuredLogger[IO] = NoOpLogger[IO]

  def successCache(weather: Weather): WeatherCacheAlgebra[IO] = 
    new WeatherCacheAlgebra[IO] {
      override def set(key: String, weather: Weather): IO[Unit] = IO.unit
      override def get(key: String): IO[Option[Weather]] = IO.pure(Some(weather))
    }

  def noHitCache: WeatherCacheAlgebra[IO] = 
    new WeatherCacheAlgebra[IO] {
      override def set(key: String, weather: Weather): IO[Unit] = IO.unit
      override def get(key: String): IO[Option[Weather]] = IO.pure(None)
    }

  def successApi(weather: Weather): WeatherApiAlgebra[IO] = 
    new WeatherApiAlgebra[IO] {
      override def get(city: City): IO[Either[String, Weather]] = IO.pure(Right(weather))
    }

  def errorApi: WeatherApiAlgebra[IO] = 
    new WeatherApiAlgebra[IO] {
      override def get(city: City): IO[Either[String, Weather]] = IO.pure(Left("Something wrong!"))
    }

  val dummyCity = City("foo")
  val dummyWeather = Weather(90, 20.0)

  test("If cache hit, then right result") {
    
    val program = WeatherProgram.make(
      successCache(dummyWeather),
      successApi(dummyWeather)
    )
    assertIO(program.getCityWeather(dummyCity), Right(dummyWeather))

    val programWithErrorApi = WeatherProgram.make(
      successCache(dummyWeather),
      errorApi
    )
    assertIO(program.getCityWeather(dummyCity), Right(dummyWeather))
  }

  test("Cache no hit and success API, then right result") {
    val program = WeatherProgram.make(
      noHitCache,
      successApi(dummyWeather)
    )
    assertIO(program.getCityWeather(dummyCity), Right(dummyWeather))
  }

  test("Cache no hit and failed API, then left result") {
    val program = WeatherProgram.make(
      noHitCache,
      errorApi
    )
    assertIO(program.getCityWeather(dummyCity), Left("Something wrong!"))
  }

}
