ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.2.2"

lazy val root = project
  .in(file("."))
  .aggregate(
    common,
    douglasNewmanGood,
    sherryFitzGerald
  )

lazy val common = project
  .settings(
    libraryDependencies ++= List(
      "com.google.cloud" % "google-cloud-storage" % "2.18.0",
      "dev.zio" %% "zio" % "2.0.6",
      "dev.zio" %% "zio-http" % "0.0.4",
      "dev.zio" %% "zio-json" % "0.4.2",
      "dev.zio" %% "zio-streams" % "2.0.6",
      "org.jsoup" % "jsoup" % "1.15.4",
      "org.scalameta" %% "munit" % "0.7.29" % Test
    )
  )

lazy val douglasNewmanGood = project
  .dependsOn(common % "compile->compile;test->test")

lazy val sherryFitzGerald = project
  .dependsOn(common % "compile->compile;test->test")
