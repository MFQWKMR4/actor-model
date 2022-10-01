import Dependencies._

ThisBuild / scalaVersion := "2.12.8"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.example"
ThisBuild / organizationName := "example"

val catsVersion = "2.4.2"
val catsCore = "org.typelevel" %% "cats-core" % catsVersion
val catsFree = "org.typelevel" %% "cats-free" % catsVersion
val catsLaws = "org.typelevel" %% "cats-laws" % catsVersion
val catsMtl = "org.typelevel" %% "cats-mtl-core" % "0.7.1"

val simulacrum = "org.typelevel" %% "simulacrum" % "1.0.1"
val kindProjector = compilerPlugin("org.typelevel" % "kind-projector" % "0.11.3" cross CrossVersion.full)
val resetAllAttrs = "org.scalamacros" %% "resetallattrs" % "1.0.0"
val munit = "org.scalameta" %% "munit" % "0.7.22"
val disciplineMunit = "org.typelevel" %% "discipline-munit" % "1.0.6"

lazy val root = (project in file("."))
    .settings(
        name := "distr",
        libraryDependencies ++= {
            val akkaVersion = "2.5.4"
            val akkaHttpVersion = "10.0.10"
            Seq(
                "com.typesafe.akka" %% "akka-actor" % akkaVersion,
                "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
                "org.scalatest" %% "scalatest" % "3.0.0" % "test",
                catsCore,
                catsFree,
                catsMtl,
                simulacrum,
                kindProjector,
                resetAllAttrs,
                catsLaws % Test,
                munit % Test,
                disciplineMunit % Test,
            )
        },
        scalacOptions ++= Seq(
            "-deprecation",
            "-encoding",
            "UTF-8",
            "-feature",
        "-language:_",
      ),
    )
