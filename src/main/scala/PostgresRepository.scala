import java.sql.{Connection, DriverManager}

/**
  * Created by yury on 03.07.17.
  */
object PostgresRepository extends App {
  val url = "jdbc:postgresql://localhost/postgres"

  Class.forName("org.postgresql.Driver")

  import resource._

  managed(DriverManager.getConnection(url, "postgres", "postgres")) acquireAndGet {
    connection =>

  }

    val connection = DriverManager.getConnection(url, "postgres", "postgres")



}
