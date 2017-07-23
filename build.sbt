name := "spark_plays"


version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.2.0" % "provided"

libraryDependencies += "org.apache.spark" % "spark-sql_2.11" % "2.2.0" % "provided"

libraryDependencies += "org.apache.kafka" % "kafka-clients" % "0.10.2.1" % "provided"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.7"

libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0"

libraryDependencies += "postgresql" % "postgresql" % "9.4.1208-atlassian-hosted"

resolvers += "9.4.1208-atlassian-hosted" at "https://maven.atlassian.com/3rdparty/"

libraryDependencies += "com.jsuereth" % "scala-arm_2.11" % "2.0"

libraryDependencies += "org.apache.spark" % "spark-sql-kafka-0-10_2.11" % "2.1.1" % "provided"

libraryDependencies += "com.google.code.gson" % "gson" % "2.8.1"