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
 * Created by bedux on 14/04/16.
 */

/**
 * Created by bedux on 06/04/16.
 */



/**
 * Created by bedux on 05/04/16.
 */

case class MethodDiscussion (idD:Option[Long], idM:Option[Long])
class MethodDiscussions(tag:Tag) extends  Table[MethodDiscussion](tag, "method_discussion"){

  import PostgresDriverWithJsonSupport.api._

  def idD = column[Long]("idd")
  def idDiscussion = foreignKey("idd", idD, DiscussionUtils.discussions)(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)

  def idM = column[Long]("idm")
  def idImport = foreignKey("idm", idM, MethodDeclaration.imports)(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)

  def id = primaryKey("id", (idM, idD))



  override def * = (idD.?,idM.?) <>((MethodDiscussion.apply _).tupled, MethodDiscussion.unapply _)
}


object MethodDiscussion{
  val methodDiscussions = TableQuery[MethodDiscussions]

  def insert(discussionToAdd: MethodDiscussion):Future[(Long,Long)] = {
    val id = (methodDiscussions returning methodDiscussions.map(x=>(x.idD,x.idM))) += discussionToAdd
    val future:Future[(Long,Long)] = DatabaseConnection.db.run(id)
    future
  }

}