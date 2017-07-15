
/**
  * Created by yury on 24.06.17.
  */
case class UserStat(userId: Int, browser: String, slot: String, clicks: Int, queries: Int, other: Int) {

  override def toString = s"UserStat($userId, $browser, $slot, $clicks, $queries, $other)"
}
