name := "csi"

version := "1.0"

lazy val `csi` = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq( javaJdbc , javaEbean , cache , javaWs)

libraryDependencies += "org.eclipse.jgit" % "org.eclipse.jgit" % "4.2.0.201601211800-r"

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.18"

libraryDependencies +=
  "com.typesafe.akka" %% "akka-actor" % "2.4.2"
