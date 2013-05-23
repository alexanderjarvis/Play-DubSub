import sbt._
import sbt.Keys._

object PlaydubsubBuild extends Build {

  lazy val playdubsub = Project(
    id = "play-dubsub",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "play-dubsub",
      organization := "uk.co.panaxiom",
      version := "0.2-SNAPSHOT",
      scalaVersion := "2.10.1",
      // add other settings here
      libraryDependencies ++= Seq(
        "play" %% "play" % "2.1.1" % "provided",
        "uk.co.panaxiom" % "dubsub" % "0.2-SNAPSHOT"
      ),
      resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"
    )
  )

}
