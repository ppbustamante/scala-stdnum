ThisBuild / organization := "cl.mixin"
ThisBuild / scalaVersion := "3.3.1"
ThisBuild / version := "0.1.0-SNAPSHOT"

lazy val root = project
  .in(file("."))
  .settings(
    name := "scala-stdnum",
    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test
  )
