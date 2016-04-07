package database

import slick.jdbc.JdbcBackend.Database

/** *
  * Base object for database connection
  */

object DatabaseConnection {
  /** *
    * Create a connection to the postgresql database
    */
  val db = Database.forURL(url = "jdbc:postgresql://localhost:5433/csi",user = "bedux", password = "f38anun4", driver = "org.postgresql.Driver" ,   keepAliveConnection = true)

  /** *
    * Use it for close connection
    */
  def dbClose = db.close

}
