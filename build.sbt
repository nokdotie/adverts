ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.2.2"

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
    libraryDependencies ++= List(
      "com.google.cloud" % "google-cloud-storage" % "2.22.3",
      "dev.zio" %% "zio" % "2.0.13",
      "dev.zio" %% "zio-http" % "0.0.4",
      "dev.zio" %% "zio-json" % "0.4.2",
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
