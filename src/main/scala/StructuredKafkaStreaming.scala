import org.apache.spark.sql.SparkSession

/**
  * Created by yury on 08.07.17.
  */
object StructuredKafkaStreaming {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession
      .builder
      .appName("ReadingKafkaData")
      .getOrCreate()

    import spark.implicits._

    // Subscribe to 1 topic
    val ds1 = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "localhost:9092")
      .option("startingOffsets", "earliest")
      .option("subscribe", "test")
      .load()
    val stringDS = ds1.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)")
      .as[(String, String)]

    // Start running the query that prints the running counts to the console
    val query = stringDS.writeStream
      .outputMode("append")
      .format("console")
      .start()

    query.awaitTermination()

  }
}
