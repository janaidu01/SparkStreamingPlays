import com.google.gson.Gson
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

    spark.sparkContext.setLogLevel("ERROR")

    val ds1 = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "localhost:9092")
      .option("startingOffsets", "earliest")
      .option("subscribe", "test")
      .load()

    spark.udf.register("deserialize", (value: Array[Byte]) => GsonHolder.gson.fromJson(new String(value), classOf[UserStat]))

    val userStatDS = ds1.select("value").selectExpr("""deserialize(value) AS data""")

    userStatDS.printSchema()

    val flattened = userStatDS.select("data.*").withWatermark("time", "10 minutes")

    flattened.printSchema()
    flattened.createOrReplaceTempView("userstat")

    val brClicks = spark.sql("select time, browser, sum(clicks) as clicks from userstat group by browser, time")

    val query = brClicks.select("browser", "clicks")
      .writeStream
      .format("parquet")
      .option("path", "results")
      .option("checkpointLocation", "checkpoints")
      .start()

    query.awaitTermination()

  }

  object GsonHolder {
    val gson = new Gson
  }
}
