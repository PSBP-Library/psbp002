val scala3Version = "3.2.2"

lazy val root = project
  .in(file("."))
  .settings(
    name := "psbp002",
    version := "0.0.1-SNAPSHOT",

    scalaVersion := scala3Version,

  )
