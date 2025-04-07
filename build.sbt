ThisBuild / organization := "cl.mixin"
ThisBuild / scalaVersion := "3.3.5"
ThisBuild / version := "1.0.0-SNAPSHOT"

lazy val root = project
  .in(file("."))
  .settings(
    name := "scala-stdnum",
    libraryDependencies += "org.scalameta" %% "munit" % "1.1.0" % Test
  )
