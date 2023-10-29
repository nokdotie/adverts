import java.time.format.DateTimeFormatter
import java.time.{Instant, ZoneOffset}

ThisBuild / scalaVersion := "3.3.1"
ThisBuild / organization := "ie.nok"
ThisBuild / version := DateTimeFormatter
  .ofPattern("yyyyMMdd.HHmmss.n")
  .withZone(ZoneOffset.UTC)
  .format(Instant.now())

lazy val root = project
  .in(file("."))
  .aggregate(
    adverts,
    aggregate,
    scraper
  )

lazy val adverts = project
  .settings(
    githubOwner := "nok-ie",
    githubRepository := "adverts",
    resolvers += Resolver.githubPackages("nok-ie"),
    libraryDependencies ++= List(
      "dev.zio" %% "zio" % "2.0.18",
      "dev.zio" %% "zio-http" % "0.0.5",
      "dev.zio" %% "zio-nio" % "2.0.2",
      "dev.zio" %% "zio-streams" % "2.0.18",
      "ie.nok" %% "scala-libraries" % "20230925.163741.873519513" % "compile->compile;test->test",
      "ie.nok" %% "building-energy-rating" % "20230925.163923.273384852" % "compile->compile;test->test",
      "ie.nok" %% "eircode-address-database" % "20231027.201525.876120335" % "compile->compile;test->test",
      "org.jsoup" % "jsoup" % "1.16.1",
      "org.scalameta" %% "munit" % "0.7.29" % Test,
      "org.scalameta" %% "munit-scalacheck" % "0.7.29" % Test
    )
  )

lazy val aggregate = project
  .dependsOn(adverts % "compile->compile;test->test")

lazy val scraper = project
  .dependsOn(adverts % "compile->compile;test->test")
