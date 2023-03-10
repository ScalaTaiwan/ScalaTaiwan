package schema

import zio.schema.*
import zio.schema.syntax.*

case class Person(name: String, age: Int)
given Schema[Person] = DeriveSchema.gen

@main def run = println(Person("John", 20).diff(Person("Johnny", 20)))
