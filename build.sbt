import _root_.play.sbt.PlayImport._

import sbt.Keys._
import sbt._

name := "scapig-api-scope"

version := "1.0"

lazy val `scapig_api_definition` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq( ws, guice )
libraryDependencies += "org.reactivemongo" %% "play2-reactivemongo" % "0.12.6-play26"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"
libraryDependencies += "org.mockito" % "mockito-all" % "1.10.19" % "test"
libraryDependencies += "com.github.tomakehurst" % "wiremock" % "2.8.0" % "component"
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.1" % "component"
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.1" % "component"
libraryDependencies += "org.scalaj" %% "scalaj-http" % "2.3.0" % "component"

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

lazy val microservice = (project in file("."))
  .enablePlugins(Seq(play.sbt.PlayScala) : _*)
  .configs(IntTest)
  .settings(inConfig(IntTest)(Defaults.testSettings): _*)
  .settings(
    Keys.fork in IntTest := false,
    unmanagedSourceDirectories in IntTest <<= (baseDirectory in IntTest) (base => Seq(base / "it"))
  )
  .configs(ComponentTest)
  .settings(inConfig(ComponentTest)(Defaults.testSettings): _*)
  .settings(
    Keys.fork in ComponentTest := false,
    unmanagedSourceDirectories in ComponentTest <<= (baseDirectory in ComponentTest) (base => Seq(base / "component"))
  )

lazy val IntTest = config("it") extend Test
lazy val ComponentTest = config("component") extend Test
