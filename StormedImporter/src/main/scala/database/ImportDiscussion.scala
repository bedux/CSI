package database

/**
 * Created by bedux on 06/04/16.
 */
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

case class ImportDiscussion (idD:Option[Long], idI:Option[Long])
class ImportDiscussions(tag:Tag) extends  Table[ImportDiscussion](tag, "import_discussion"){

  import PostgresDriverWithJsonSupport.api._

  def idD = column[Long]("idd")
  def idDiscussion = foreignKey("idd", idD, DiscussionUtils.discussions)(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)

  def idI = column[Long]("idi")
  def idImport = foreignKey("idi", idI, ImportDeclaration.imports)(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)

  def id = primaryKey("id", (idI, idD))



  override def * = (idD.?,idI.?) <>((ImportDiscussion.apply _).tupled, ImportDiscussion.unapply _)
}


object ImportDiscussion{
  val importDiscussions = TableQuery[ImportDiscussions]

  def insert(discussionToAdd: ImportDiscussion):Future[(Long,Long)] = {
    println("Inserting :",discussionToAdd)

    val id = (importDiscussions returning importDiscussions.map(x=>(x.idI,x.idD))) += discussionToAdd
    val future:Future[(Long,Long)] = DatabaseConnection.db.run(id)
    future
  }

}