import sbt._
import sbt.Keys._
import xerial.sbt.Pack._


object Build extends Build {

  val commonSettings = Defaults.coreDefaultSettings ++ Seq(
    scalaVersion := "2.11.4",
    crossScalaVersions := Seq("2.11.4", "2.10.3"),
    version := "0.1",
    crossPaths := false
  )

  val shellScripts =Map("psychic-pocker-player"->"com.bitmain.psychic.pocker.main.Main")

  lazy val root = Project(
    id = "psychic-pocker-player",
    base = file("."),
    settings = commonSettings ++ packAutoSettings ++
      Seq(
        // custom settings here
        packMain := shellScripts,
        packBashTemplate := "template/launch.mustache",
        libraryDependencies ++= Seq("org.xerial" % "xerial-core" % "3.3.6",
          "org.xerial.snappy" % "snappy-java" % "1.1.1.6",
          "org.scalatest" % "scalatest_2.11" % "3.0.1")
      )
  ) dependsOn()

}
