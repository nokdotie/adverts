import java.time.format.DateTimeFormatter
import java.time.{Instant, ZoneOffset}

ThisBuild / scalaVersion := "3.3.0"
ThisBuild / organization := "ie.nok"
ThisBuild / version := DateTimeFormatter
  .ofPattern("yyyyMMdd.HHmmss.n")
  .withZone(ZoneOffset.UTC)
  .format(Instant.now())

lazy val root = project
  .in(file("."))
  .aggregate(
    common,
    daft,
    douglasNewmanGood,
    myHome,
    sherryFitzGerald
  )

lazy val common = project
  .settings(
    resolvers += Resolver.githubPackages("nok-ie"),
    libraryDependencies ++= List(
      "com.google.cloud" % "google-cloud-storage" % "2.18.0",
      "dev.zio" %% "zio" % "2.0.13",
      "dev.zio" %% "zio-http" % "0.0.4",
      "dev.zio" %% "zio-json" % "0.4.2",
      "dev.zio" %% "zio-streams" % "2.0.13",
      "ie.nok" %% "scala-libraries" % "20230610.102516.252288962",
      "org.jsoup" % "jsoup" % "1.15.3",
      "org.scalameta" %% "munit" % "0.7.29" % Test
    )
  )

lazy val adverts = project
  .dependsOn(common % "compile->compile;test->test")
  .settings(
    githubOwner := "nok-ie",
    githubRepository := "adverts"
  )

lazy val daft = project
  .dependsOn(adverts, common % "compile->compile;test->test")

lazy val douglasNewmanGood = project
  .dependsOn(adverts, common % "compile->compile;test->test")

lazy val myHome = project
  .dependsOn(adverts, common % "compile->compile;test->test")

lazy val propertyPal = project
  .dependsOn(adverts, common % "compile->compile;test->test")

lazy val sherryFitzGerald = project
  .dependsOn(adverts, common % "compile->compile;test->test")
