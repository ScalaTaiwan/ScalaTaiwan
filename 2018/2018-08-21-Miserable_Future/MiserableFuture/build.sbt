name := "MiserableFuture"

scalaVersion := "2.12.6"

libraryDependencies += "com.storm-enroute" %% "scalameter" % "0.10.1"

parallelExecution in Test := false
logBuffered := false
testFrameworks += new TestFramework("org.scalameter.ScalaMeterFramework")

scalacOptions ++= Seq(
  "-deprecation",
  "-unchecked",
  "-encoding",
  "UTF-8",
  "-feature",
  "-language:postfixOps",
  "-language:implicitConversions",
  "-language:higherKinds",
  "-Xlint",
  "-Ypartial-unification"
)
