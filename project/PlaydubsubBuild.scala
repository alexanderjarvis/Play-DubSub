import sbt._
import sbt.Keys._

object PlaydubsubBuild extends Build {

  lazy val playdubsub = Project(
    id = "play-dubsub",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "play-dubsub",
      organization := "uk.co.panaxiom",
      version := "0.3-SNAPSHOT",
      scalaVersion := "2.10.2",
      // add other settings here
      libraryDependencies ++= Seq(
        "com.typesafe.play" %% "play" % "2.2.1" % "provided",
        "uk.co.panaxiom" % "dubsub" % "0.2-SNAPSHOT"
      ),
      resolvers ++= Seq("Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
                    Resolver.url("Alex's GitHub Repository", url("http://alexanderjarvis.github.com/snapshots/"))(Resolver.ivyStylePatterns))
    )
  )

}
