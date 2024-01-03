import java.time.format.DateTimeFormatter
import java.time.{Instant, ZoneOffset}
import scala.sys.process._

ThisBuild / scalaVersion := "3.3.1"
ThisBuild / organization := "ie.nok"
ThisBuild / version := DateTimeFormatter
  .ofPattern("yyyyMMdd.HHmmss.n")
  .withZone(ZoneOffset.UTC)
  .format(Instant.now())

lazy val scalafmtConfigSetting = scalafmtConfig := ({
  val source = url("https://raw.githubusercontent.com/nokdotie/gists/main/sbt/.scalafmt.conf")
  val destination = file("target/scalafmt.conf")

  source #> destination !!;
  destination
})

lazy val root = project
  .in(file("."))
  .aggregate(
    adverts,
    aggregate,
    scraper
  )
  .settings(scalafmtConfigSetting)

lazy val adverts = project
  .settings(
    githubOwner      := "nokdotie",
    githubRepository := "adverts",
    resolvers += Resolver.githubPackages("nokdotie"),
    libraryDependencies ++= List(
      "dev.zio" %% "zio" % "2.0.20",
      "dev.zio" %% "zio-http" % "0.0.5",
      "dev.zio" %% "zio-nio" % "2.0.2",
      "dev.zio" %% "zio-streams" % "2.0.20",
      "ie.nok" %% "scala-libraries" % "20231029.200446.985541447" % "compile->compile;test->test",
      "ie.nok" %% "building-energy-rating" % "20231108.094613.551868231" % "compile->compile;test->test",
      "ie.nok" %% "eircode-address-database" % "20231029.200503.950433847" % "compile->compile;test->test",
      "org.jsoup" % "jsoup" % "1.17.1",
      "org.scalameta" %% "munit" % "0.7.29" % Test,
      "org.scalameta" %% "munit-scalacheck" % "0.7.29" % Test
    ),
    scalafmtConfigSetting
  )

lazy val aggregate = project
  .dependsOn(adverts % "compile->compile;test->test")
  .settings(scalafmtConfigSetting)

lazy val scraper = project
  .dependsOn(adverts % "compile->compile;test->test")
  .settings(scalafmtConfigSetting)
