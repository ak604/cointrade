name := """cointrade"""
organization := "com.xyz"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.3"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
libraryDependencies += ws
libraryDependencies += "io.reactivex" %% "rxscala" % "0.26.5"
EclipseKeys.preTasks := Seq(compile in Compile, compile in Test)
libraryDependencies += javaJdbc
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.41"
libraryDependencies += jdbc
libraryDependencies += "com.typesafe.play" %% "anorm" % "2.5.3" withSources()
libraryDependencies += "org.mockito" % "mockito-core" % "2.12.0" % Test

lazy val myProject = (project in file("."))
  .enablePlugins(PlayJava, PlayEbean)