package weather

import cats.effect.{ExitCode, IO, IOApp}
import weather.http.WeatherServer
import org.typelevel.log4cats.SelfAwareStructuredLogger
import org.typelevel.log4cats.slf4j.Slf4jLogger

object Main extends IOApp.Simple:

  given logger: SelfAwareStructuredLogger[IO] = Slf4jLogger.getLogger[IO]
  val run = WeatherServer.run[IO]
