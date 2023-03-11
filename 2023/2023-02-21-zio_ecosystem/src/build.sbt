name := "zio-ecosystem"

scalaVersion := "3.2.0"

libraryDependencies ++= List(
  "dev.zio" %% "zio-json" % "0.4.2",
  "dev.zio" %% "zio-http" % "0.0.4",
  "dev.zio" %% "zio-schema" % "0.4.8",
  "dev.zio" %% "zio-aws-netty" % "5.19.33.1",
  "dev.zio" %% "zio-aws-s3" % "5.19.33.1",

  "com.github.ghostdogpr" %% "caliban" % "2.0.2",
  "com.github.ghostdogpr" %% "caliban-zio-http" % "2.0.2"
)
