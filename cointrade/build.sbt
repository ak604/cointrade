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
libraryDependencies ++= Seq(
  javaJpa,
  "org.hibernate" % "hibernate-entitymanager" % "5.2.12.Final"
)
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.41"
// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.xyz.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.xyz.binders._"
