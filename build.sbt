name := "csi"

version := "1.0"

lazy val `csi` = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq( javaJdbc , javaEbean , cache , javaWs)

libraryDependencies += "org.eclipse.jgit" % "org.eclipse.jgit" % "4.2.0.201601211800-r"

libraryDependencies += "org.postgresql" % "postgresql" % "9.4.1208.jre7"

libraryDependencies += "com.github.javaparser" % "javaparser-core" % "2.3.0"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.4.2"

libraryDependencies += "javax.inject" % "javax.inject" % "1"
