
ThisBuild / scalaVersion     := "2.13.5"
ThisBuild / version          := "0.1.0-SNAPSHOT"

lazy val akkaVersion = "2.6.11"

lazy val root = (project in file("."))
  .settings(
    name := "reflect",

    libraryDependencies ++= Seq(
        "org.scala-lang" % "scala-library" % scalaVersion.value,
        "org.scala-lang" % "scala-compiler" % scalaVersion.value,
        "org.scala-lang" % "scala-reflect" % scalaVersion.value,

        "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
        "com.typesafe.akka" %% "akka-stream" % akkaVersion,
        "ch.qos.logback" % "logback-classic" % "1.2.3",
    )

  )
