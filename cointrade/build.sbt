name := """cointrade"""
organization := "com.xyz"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.3"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
libraryDependencies += ws
libraryDependencies += "io.reactivex" %% "rxscala" % "0.26.5"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.xyz.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.xyz.binders._"
