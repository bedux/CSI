package database

import java.util.concurrent.Executors

import slick.jdbc.JdbcBackend.Database

import scala.concurrent.ExecutionContext

/** *
  * Base object for database connection
  */



object DatabaseConnection {
  /** *
    * Create a connection to the postgresql database
    */
 val db = Database.forURL(url = "jdbc:postgresql://localhost:5432/csi",user = "student_account", password = "student_account", driver = "org.postgresql.Driver" ,   keepAliveConnection = true)
  //val db = Database.forURL(url = "jdbc:postgresql://localhost:5433/csi",user = "bedux", password = "f38anun4", driver = "org.postgresql.Driver" ,   keepAliveConnection = true)

  implicit val ec = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(3))

  /** *
    * Use it for close connection
    */
  def dbClose = db.close

}
