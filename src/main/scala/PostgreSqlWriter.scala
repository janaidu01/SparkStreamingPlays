import java.sql.{DriverManager, PreparedStatement}

import org.apache.spark.sql.{ForeachWriter, Row}
import resource.managed

object PostgreSqlWriter extends ForeachWriter[Row] {
  val url = "jdbc:postgresql://localhost/postgres"

  override def open(partitionId: Long, version: Long): Boolean = {
    classOf[(org.postgresql.Driver)]
    managed(DriverManager.getConnection(url, "postgres", "postgres")) acquireAndGet {
      connection =>
        val statement = connection
          .prepareStatement(s"CREATE TABLE IF NOT EXISTS browser_clicks( browser varchar(100), clicks integer )")

        statement.execute()
    }
    true
  }

  override def process(value: Row): Unit = {
    println(s"row size: ${value.size}")
    managed(DriverManager.getConnection(url, "postgres", "postgres")) acquireAndGet {
      connection =>
        val statement = connection
          .prepareStatement(s"INSERT INTO browser_clicks (browser, clicks) VALUES ( '${value.getString(0)}', ${value.getLong(1)})")

        val result = statement.executeUpdate()
        println(s"result int: $result")
    }
  }

  override def close(errorOrNull: Throwable): Unit = {}
}
