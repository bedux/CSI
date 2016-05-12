package database

import slick.jdbc.JdbcBackend.Database

/** *
  * Base object for database connection
  */

object DatabaseConnection {
  /** *
    * Create a connection to the postgresql database
    */
  val db = Database.forURL(url = "jdbc:postgresql://localhost:5432/csi",user = "student_account", password = "student_account", driver = "org.postgresql.Driver" ,   keepAliveConnection = true)

  /** *
    * Use it for close connection
    */
  def dbClose = db.close

}
