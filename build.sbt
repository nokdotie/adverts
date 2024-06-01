import java.time.format.DateTimeFormatter
import java.time.{Instant, ZoneOffset}

ThisBuild / scalaVersion := "3.3.3"
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
    scraper,
    scraper2,
    seo
  )

lazy val adverts = project
  .settings(
    githubOwner      := "nokdotie",
    githubRepository := "adverts",
    resolvers += Resolver.githubPackages("nokdotie"),
    libraryDependencies ++= List(
      "dev.zio"       %% "zio"                      % "2.0.22",
      "dev.zio"       %% "zio-http"                 % "0.0.5",
      "dev.zio"       %% "zio-nio"                  % "2.0.2",
      "dev.zio"       %% "zio-streams"              % "2.0.22",
      "ie.nok"        %% "scala-libraries"          % "20240416.093923.731176565" % "compile->compile;test->test",
      "ie.nok"        %% "building-energy-rating"   % "20240416.095216.870755440" % "compile->compile;test->test",
      "ie.nok"        %% "eircode-address-database" % "20240307.163929.558029779" % "compile->compile;test->test",
      "org.jsoup"      % "jsoup"                    % "1.17.2",
      "org.scalameta" %% "munit"                    % "1.0.0"                     % Test,
      "org.scalameta" %% "munit-scalacheck"         % "1.0.0"                     % Test
    )
  )

lazy val aggregate = project
  .dependsOn(adverts % "compile->compile;test->test")

lazy val scraper = project
  .dependsOn(adverts % "compile->compile;test->test")

lazy val scraper2 = project
  .dependsOn(adverts % "compile->compile;test->test")

lazy val seo = project
  .dependsOn(adverts % "compile->compile;test->test")
