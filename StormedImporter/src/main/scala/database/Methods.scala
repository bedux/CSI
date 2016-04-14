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
  * @param methodName string contains the name of the package
  *
  */
case class MethodDeclaration (id:Option[Long], methodName: String , params:Long)
class MethodDeclarations(tag:Tag) extends  Table[MethodDeclaration](tag,"method"){

  import PostgresDriverWithJsonSupport.api._

  def id = column[Long]("id",O.AutoInc,O.PrimaryKey)
  def params = column[Long]("params")

  def methodName = column[String]("methodname")

  override def * = (id.?,methodName,params) <> ((MethodDeclaration.apply _).tupled, MethodDeclaration.unapply)

}

object MethodDeclaration{

  //TODO
  val imports = TableQuery[MethodDeclarations]

  def insert(methodDeclarationToAdd: MethodDeclaration):Future[Long] = {
    println("Inserting :",methodDeclarationToAdd)
    val  future:Future[Seq[MethodDeclaration]] = findByName(methodDeclarationToAdd.methodName)
    val futureLong:Future[Long] = future.flatMap[Long]( x=>x match {
        case l if(l.size>0) =>Future[Long](l(0).id.get)
        case _ => {
           val c = (imports returning imports.map(_.id))+= methodDeclarationToAdd
           val futureResult :Future[Long] = DatabaseConnection.db.run(c)
           futureResult
        }
    })
    futureLong
  }

  def getAll:Future[Seq[MethodDeclaration]]= {
    val q = (for (c <- imports) yield c).result
    val c:Future[Seq[MethodDeclaration]]= DatabaseConnection.db.run(q)
    c
  }

  def findByName(name:String):Future[Seq[MethodDeclaration]]={
    val q = MethodDeclaration.imports.filter(_.methodName === name).result
    DatabaseConnection.db.run(q)
  }

}

