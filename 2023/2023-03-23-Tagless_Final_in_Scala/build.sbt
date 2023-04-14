val Http4sVersion = "0.23.6"
val MunitVersion = "0.7.29"
val LogbackVersion = "1.2.6"
val MunitCatsEffectVersion = "1.0.6"
val circeVersion = "0.14.1"
val log4catsVersion = "2.5.0"

lazy val root = (project in file("."))
  .settings(
    organization := "com.seansun",
    name := "tagless-final-weather-api",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "3.2.1",
    libraryDependencies ++= Seq(
      "org.http4s"      %% "http4s-ember-server" % Http4sVersion,
      "org.http4s"      %% "http4s-ember-client" % Http4sVersion,
      "org.http4s"      %% "http4s-circe"        % Http4sVersion,
      "org.http4s"      %% "http4s-dsl"          % Http4sVersion,
      "org.scalameta"   %% "munit"               % MunitVersion           % Test,
      "org.typelevel"   %% "munit-cats-effect-3" % MunitCatsEffectVersion % Test,
      "ch.qos.logback"  %  "logback-classic"     % LogbackVersion,
      "org.typelevel"   %% "log4cats-slf4j"      % log4catsVersion,
      "org.typelevel"   %% "log4cats-noop"       % log4catsVersion,
      "io.circe"        %% "circe-core"          % circeVersion,
      "io.circe"        %% "circe-generic"       % circeVersion,
      "io.circe"        %% "circe-parser"        % circeVersion,
    ),
    testFrameworks += new TestFramework("munit.Framework")
  )
