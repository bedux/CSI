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
import scala.concurrent.ExecutionContext.Implicits.global

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
    val cane :Future[(Long,Long)] = for{
      c <- ImportDiscussion.findByName(discussionToAdd.idD.get,discussionToAdd.idI.get)
      d <-
        if(c.isEmpty){
        val id = (importDiscussions returning importDiscussions.map(x=>(x.idD,x.idI))) += discussionToAdd
        val l : Future[(Long,Long)]=DatabaseConnection.db.run(id)
        l
      }else{
          val l : Future[(Long,Long)]=Future{(c.head.idD.get,c.head.idI.get)}
          l

      }

    }yield d
    cane

//      val ca =findByName(discussionToAdd.idD.get,discussionToAdd.idI.get)
//      Await.result(ca,Duration.Inf);
//      if(ca.value.get.get.nonEmpty){
//        print("Found")
//        Future{(1L,1L)}
//      }else {
//        print("Insert")
//        val c = (importDiscussions returning importDiscussions.map(x=>(x.idI,x.idD))) += discussionToAdd
//        val e: Future[(Long,Long)] = DatabaseConnection.db.run(c)
//        Await.result(e,Duration.Inf);
//        Future{(1L,1L)}
//      }


//      val id = (methodDiscussions returning methodDiscussions.map(x=>(x.idD,x.idM))) += discussionToAdd
//      val future:Future[(Long,Long)] = DatabaseConnection.db.run(id)
//      future
    }

    def findByName(idd: Long,idm: Long): Future[Seq[ImportDiscussion]] = {
      val q = importDiscussions.filter(x => x.idD === idd &&  x.idI === idm ).result
      val r = DatabaseConnection.db.run(q)
      Await.ready(r,Duration.Inf)
      r
    }

}