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
    propertyPal,
    sherryFitzGerald
  )

lazy val adverts = project
  .settings(
    githubOwner := "nok-ie",
    githubRepository := "adverts",
    libraryDependencies ++= List(
      "ie.nok" %% "scala-libraries" % "20230613.144229.725738162"
    )
  )

lazy val common = project
  .dependsOn(adverts)
  .settings(
    resolvers += Resolver.githubPackages("nok-ie"),
    libraryDependencies ++= List(
      "com.google.cloud" % "google-cloud-storage" % "2.22.4",
      "dev.zio" %% "zio" % "2.0.13",
      "dev.zio" %% "zio-http" % "0.0.4",
      "dev.zio" %% "zio-nio" % "2.0.1",
      "dev.zio" %% "zio-streams" % "2.0.13",
      "org.jsoup" % "jsoup" % "1.15.3",
      "org.scalameta" %% "munit" % "0.7.29" % Test
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
