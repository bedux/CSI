import _root_.sbt.Keys._
import sbtassembly.MergeStrategy


name := "StormedImporter"

version := "1.0"

scalaVersion := "2.11.8"


resolvers +=  "StORMeD Dev-Kit Repository" at "http://stormed.inf.usi.ch/releases/"

libraryDependencies  += "ch.usi.inf.reveal.parsing" % "stormed-devkit" % "1.9.1"
libraryDependencies +=   "com.typesafe.slick" %% "slick" % "3.1.1"
libraryDependencies  += "com.github.tminglei"		%%	"slick-pg"				%	"0.12.0"
libraryDependencies  += "com.github.tminglei" 	%% "slick-pg_json4s" 		% 	"0.12.0"
libraryDependencies +=   "org.slf4j" % "slf4j-nop" % "1.6.4"


mainClass in assembly := Some("database.main")


assemblyMergeStrategy in assembly := {
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  case _ => MergeStrategy.first


}