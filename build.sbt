name := "play-dubsub"

organization := "uk.co.panaxiom"

version := "0.4-SNAPSHOT"

scalaVersion := "2.11.1"

crossScalaVersions := Seq("2.11.1", "2.10.4")

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play" % "2.3.1" % "provided",
  "uk.co.panaxiom" %% "dubsub" % "0.4-SNAPSHOT"
)