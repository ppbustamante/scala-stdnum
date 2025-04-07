ThisBuild / organization := "cl.mixin"
ThisBuild / scalaVersion := "3.3.5"
ThisBuild / version := "1.0.0"
ThisBuild / versionScheme := Some("semver-spec")

ThisBuild / homepage := Some(url("https://github.com/ppbustamante/scala-stdnum"))
ThisBuild / licenses := Seq(
  "LICENSE" -> url("https://github.com/ppbustamante/scala-stdnum/blob/main/LICENSE")
)
ThisBuild / publishMavenStyle := true
ThisBuild / pomIncludeRepository := { _ => false }

ThisBuild / publishTo := Some(
  "GitHub ppbustamante Apache Maven Packages" at "https://maven.pkg.github.com/ppbustamante/scala-stdnum"
)
ThisBuild / credentials += Credentials(
  "GitHub Package Registry",
  "maven.pkg.github.com",
  "ppbustamante",
  System.getenv("GITHUB_TOKEN")
)

lazy val root = project
  .in(file("."))
  .settings(
    name := "scala-stdnum",
    libraryDependencies += "org.scalameta" %% "munit" % "1.1.0" % Test
  )
