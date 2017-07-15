import com.google.gson.Gson
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{StringType, StructField, StructType}

/**
  * Created by yury on 08.07.17.
  */
object StructuredKafkaStreaming {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession
      .builder
      .appName("ReadingKafkaData")
      .getOrCreate()

    spark.sparkContext.setLogLevel("ERROR")

    import spark.implicits._

    val dictionary = Seq(Country("key", "Russia"), Country("2", "Germany"), Country("key", "USA")).toDS

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
      .join(dictionary, "key")

//    val gson = new Gson()
//    val userStatDS = stringDS.map(row => gson.fromJson(row._2, UserStat.getClass) )
    // Start running the query that prints the running counts to the console
    val query = stringDS.writeStream
      .outputMode("append")
      .format("console")
//      .option("path", "results")
//      .option("checkpointLocation", "checkpoints")
      .start()

    query.awaitTermination()

  }

  case class Country(key: String, country: String)
}
