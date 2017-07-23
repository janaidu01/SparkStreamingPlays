import java.sql.Timestamp

/**
  * Created by yury on 24.06.17.
  */
case class UserStat(userId: Int, browser: String, slot: String, clicks: Int, queries: Int, other: Int, time: Timestamp) {

  override def toString = s"UserStat($userId, $browser, $slot, $clicks, $queries, $other, $time)"
}
