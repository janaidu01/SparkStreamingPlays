import com.google.gson.Gson
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.StructType

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

    // Subscribe to 1 topic
    val ds1 = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "localhost:9092")
      .option("startingOffsets", "earliest")
      .option("subscribe", "test")
      .load()
//    val stringDS = ds1.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)")
//      .as[(String, String)]

    spark.udf.register("deserialize", (value: Array[Byte]) => UserStatDeserializerWrapper.deser.deserialize(value))

    val userStatDS = ds1.select("value").selectExpr("""deserialize(value) AS data""")//.as[UserStat]

//    import org.apache.spark.sql.catalyst.ScalaReflection
//    val schema = ScalaReflection.schemaFor[UserStat].dataType.asInstanceOf[StructType]

//    spark.createDataFrame(userStatDS, schema)
//    implicit val myObjEncoder = org.apache.spark.sql.Encoders.kryo[UserStat ]
//
//    val userStatDS = stringDS.map {row => new Gson().fromJson(row._2, classOf[UserStat]) } (myObjEncoder)
    // Start running the query that prints the running counts to the console
    val query = userStatDS.writeStream
      .outputMode("append")
      .format("console")
//      .option("path", "results")
//      .option("checkpointLocation", "checkpoints")
      .start()

    query.awaitTermination()

  }

  object UserStatDeserializerWrapper {
    val deser = new UserStatDeserializer
  }

  class UserStatDeserializer (){

    def deserialize (value: Array[Byte]): UserStat = {
      println(new String(value))
      val gson = new Gson()
      gson.fromJson(new String(value), classOf[UserStat])
    }
  }
}
