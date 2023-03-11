package http

import zio.*
import zio.http.*
import zio.http.model.Method

object HttpServer extends ZIOAppDefault:

  val app: HttpApp[Any, Nothing] = Http.collect[Request] {
    case Method.GET -> !! / "hello" => Response.text("Hi, how are you?")
  }

  def run = Server.serve(app).provide(Server.default)

object HttpClient extends ZIOAppDefault:

  val app = Client
    .request(
      "https://raw.githubusercontent.com/zio/zio-http/main/zio-http-example/src/main/scala/example/AuthenticationClient.scala"
    )
    .flatMap(_.body.asString)
    .flatMap(s => Console.printLine(s))

  def run = app.provide(Client.default)
