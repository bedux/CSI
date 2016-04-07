package database


import com.sun.org.apache.xalan.internal.utils.FeatureManager.Feature
import org.json4s.scalap.Failure
import slick.driver.PostgresDriver.api._
import slick.lifted.{TableQuery}
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}
import ExecutionContext.Implicits.global

/** *
  *
  * @param id Optional, Set as AutoIncrement in the database column definition
  * @param packageImport string contains the name of the package
  *
  */
case class ImportDeclaration (id:Option[Long], packageImport: String)
class ImportDeclarations(tag:Tag) extends  Table[ImportDeclaration](tag,"IMPORT"){

  import PostgresDriverWithJsonSupport.api._

  def id = column[Long]("id",O.AutoInc,O.PrimaryKey)
  def packageImport = column[String]("package")
  
  override def * = (id.?,packageImport) <> ((ImportDeclaration.apply _).tupled, ImportDeclaration.unapply)
  
}

object ImportDeclaration{
  
  val imports = TableQuery[ImportDeclarations]

  def insert(importDeclarationToAdd: ImportDeclaration):Future[Long] = {
    val  future:Future[Seq[ImportDeclaration]] = findByName(importDeclarationToAdd.packageImport)
    val futureLong:Future[Long] = future.flatMap[Long]( x=>x match {
        case l if(l.size>0) =>Future[Long](l(0).id.get)
        case _ => {
           val c = (imports returning imports.map(_.id))+= importDeclarationToAdd
           val futureResult :Future[Long] = DatabaseConnection.db.run(c)
           futureResult
        }
    })
    futureLong
  }

  def getAll:Future[Seq[ImportDeclaration]]= {
    val q = (for (c <- imports) yield c).result
    val c:Future[Seq[ImportDeclaration]]= DatabaseConnection.db.run(q)
    c
  }

  def findByName(name:String):Future[Seq[ImportDeclaration]]={
   val q = ImportDeclaration.imports.filter(_.packageImport===name).result
    DatabaseConnection.db.run(q)
  }

}

