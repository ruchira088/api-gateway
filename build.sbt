import sbt.Keys._
import sbt._
import sbtrelease.Version

name := "hello"

resolvers += Resolver.sonatypeRepo("public")
scalaVersion := "2.12.4"
releaseNextVersion := { ver => Version(ver).map(_.bumpMinor.string).getOrElse("Error") }
assemblyJarName in assembly := "hello.jar"

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-lambda-java-events" % "1.3.0",
  "com.amazonaws" % "aws-lambda-java-core" % "1.1.0",
  "com.amazonaws" % "aws-java-sdk-dynamodb" % "1.11.256",
  "com.typesafe.akka" %% "akka-http" % "10.0.11",
  "com.typesafe.akka" %% "akka-stream" % "2.5.8",
  "com.typesafe.akka" %% "akka-actor" % "2.5.8",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.11",
  "com.typesafe.play" %% "play-json" % "2.6.8",
  "org.reactivemongo" %% "reactivemongo-play-json" % "0.12.7-play26",
  "org.reactivemongo" %% "reactivemongo" % "0.12.7",
  "com.github.etaty" %% "rediscala" % "1.8.0",
  "org.scalaz" %% "scalaz-core" % "7.2.18",
  "com.google.inject" % "guice" % "4.1.0",
  "org.mindrot" % "jbcrypt" % "0.4",
  "org.slf4j" % "log4j-over-slf4j" % "1.7.25"
)

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-feature",
  "-Xfatal-warnings")
