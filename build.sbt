name := "MindWave"

organization := "net.addictivesoftware.mindwave"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.10.1"

resolvers ++= Seq(
    "Typesafe" at "http://repo.typesafe.com/typesafe/releases/",
    "Scala Tools" at "https://oss.sonatype.org/content/groups/scala-tools/",
    "Sonatype" at "http://oss.sonatype.org/content/repositories/releases",
    "Local Repo" at "file:///Users/gertjan/.m2/repository"
)

libraryDependencies <+= scalaVersion { "org.scala-lang" % "scala-swing" % _ }

libraryDependencies ++= Seq(
	"com.typesafe" %% "scalalogging-slf4j" % "1.0.1",
    "ch.qos.logback" % "logback-classic" % "0.9.27"
)
