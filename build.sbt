name := "spark_plays"


version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.1.1" % "provided"

libraryDependencies += "org.apache.spark" % "spark-streaming_2.11" % "2.1.1" % "provided"

libraryDependencies += "org.apache.spark" % "spark-streaming-kafka-0-10_2.11" % "2.1.1" % "provided"

libraryDependencies += "org.apache.kafka" % "kafka-clients" % "0.10.2.1" % "provided"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.7"

libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0"
