import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent

/**
  * Created by yury on 14.06.17.
  */
object KafkaStreaming {
  def main(args: Array[String]): Unit = {

    val log = com.typesafe.scalalogging.Logger("KafkaStreaming")

    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "localhost:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "1",
      "auto.offset.reset" -> "earliest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )

    val conf = new SparkConf().setAppName("kafkastreaming").setMaster("local[4]")
    val ssc = new StreamingContext(conf, Seconds(1))

    val topics = Array("test")
    val stream = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams)
    )

    stream.map(record => (record.key, record.value)).print()

    ssc.start()             // Start the computation
    ssc.awaitTermination()  // Wait for the computation to terminate
  }

}
