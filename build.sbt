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
    adverts,
    common,
    daft,
    douglasNewmanGood,
    myHome,
    propertyPal,
    sherryFitzGerald
  )

lazy val adverts = project
  .settings(
    githubOwner := "nok-ie",
    githubRepository := "adverts",
    libraryDependencies ++= List(
      "ie.nok" %% "scala-libraries" % "20230614.143350.651178744",
      "org.scalameta" %% "munit" % "0.7.29" % Test,
      "org.scalameta" %% "munit-scalacheck" % "0.7.29" % Test
    )
  )

lazy val common = project
  .dependsOn(adverts)
  .settings(
    resolvers += Resolver.githubPackages("nok-ie"),
    libraryDependencies ++= List(
      "com.google.cloud" % "google-cloud-storage" % "2.22.5",
      "dev.zio" %% "zio" % "2.0.15",
      "dev.zio" %% "zio-http" % "0.0.5",
      "dev.zio" %% "zio-nio" % "2.0.1",
      "dev.zio" %% "zio-streams" % "2.0.15",
      "org.jsoup" % "jsoup" % "1.16.1"
    )
  )

lazy val daft = project
  .dependsOn(common % "compile->compile;test->test")

lazy val douglasNewmanGood = project
  .dependsOn(common % "compile->compile;test->test")

lazy val myHome = project
  .dependsOn(common % "compile->compile;test->test")

lazy val propertyPal = project
  .dependsOn(common % "compile->compile;test->test")

lazy val sherryFitzGerald = project
  .dependsOn(common % "compile->compile;test->test")
