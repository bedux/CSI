package database


import slick.ast.ColumnOption.AutoInc
import slick.backend.DatabasePublisher
import slick.driver.PostgresDriver.api._
import org.json4s.JsonAST.JValue
import database.PostgresDriverWithJsonSupport
import slick.lifted.{TableQuery, ProvenShape}
import database.{DatabaseConnection}
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

/**
 * Created by bedux on 05/04/16.
 */

case class Discussion (id:Option[Long], url:String)
class Discussions(tag:Tag) extends  Table[Discussion](tag, "DISCUSSION"){

  import PostgresDriverWithJsonSupport.api._
  import PostgresDriverWithJsonSupport.jsonMethods._

  def id = column[Long]("id",O.PrimaryKey,O.AutoInc)
  def url = column[String]("url")


  override def * = (id.?,url) <>((Discussion.apply _).tupled, Discussion.unapply _)
}

object DiscussionUtils{
  val discussions = TableQuery[Discussions]

  def insert(discussionToAdd: Discussion):Future[Long] = {
    val id = (discussions returning discussions.map(_.id)) += discussionToAdd
    val future:Future[Long] = DatabaseConnection.db.run(id)
    future
  }
}


