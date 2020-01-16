name := "lensing"
version := "0.1"
scalaVersion := "2.13.1"

libraryDependencies ++= List(
  "com.github.julien-truffaut"  %%  "monocle-core"    % "2.0.1",
  "com.github.julien-truffaut"  %%  "monocle-generic" % "2.0.1",
  "com.github.julien-truffaut"  %%  "monocle-macro"   % "2.0.1",
  "com.github.julien-truffaut"  %%  "monocle-state"   % "2.0.1",
  "com.github.julien-truffaut"  %%  "monocle-refined" % "2.0.1",
  "com.github.julien-truffaut"  %%  "monocle-unsafe"  % "2.0.1",
  "com.lihaoyi" %% "pprint" % "0.5.6"
)
