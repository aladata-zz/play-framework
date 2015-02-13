name := """plank-play-app"""

version := "1.0-SNAPSHOT"


lazy val plankmodel = (project in file("modules/plankmodel"))
  .enablePlugins(PlayJava)

lazy val plankdal = (project in file("modules/dal"))
  .enablePlugins(PlayJava)

lazy val plank = (project in file(".")).enablePlugins(PlayJava).aggregate(plankdal).aggregate(plankmodel)
  .dependsOn(plankdal).dependsOn(plankmodel)



scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  filters,
  "org.postgresql" % "postgresql" % "9.3-1102-jdbc4"
)
