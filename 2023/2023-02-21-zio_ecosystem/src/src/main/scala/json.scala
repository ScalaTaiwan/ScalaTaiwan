package json

import zio.json.*

@jsonMemberNames(SnakeCase)
case class Person(
    fullName: String,
    @jsonField("number_of_years") age: Int
) derives JsonCodec

@jsonDiscriminator("type")
enum Message derives JsonCodec:
  case Init(id: String)
  case SendConfig(num: Int, timeout: Int)
  case Start(name: String)

case class Process(message: Message) derives JsonCodec

@main def run =
  val p = Person("John", 43)
  println(p.toJsonPretty)

  val msg = Process(Message.Init("123"))
  println(msg.toJsonPretty)
