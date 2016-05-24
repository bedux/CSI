package database

import com.sun.org.apache.xalan.internal.utils.FeatureManager.Feature
import org.json4s.scalap.Failure
import slick.driver.PostgresDriver.api._
import slick.lifted.TableQuery

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success, Try}
import ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration





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

  private object Locker

  def insert(methodDeclarationToAdd: MethodDeclaration):Future[Long] = {
      for {
        exist <- findByNameAndParam(methodDeclarationToAdd.methodName, methodDeclarationToAdd.params)
        s <- if (exist.nonEmpty) {
          Future {
            exist.head.id.get
          }
        } else {

          val c = (imports returning imports.map(_.id)) += methodDeclarationToAdd
          val e: Future[Long] = DatabaseConnection.db.run(c)
          e
        }
      } yield s



//    println("TRYING INSERT ",methodDeclarationToAdd.methodName)
//    val ca = MethodDeclaration.findByNameAndParam(methodDeclarationToAdd.methodName,methodDeclarationToAdd.params)
//    Await.result(ca,Duration.Inf);
//      if(ca.value.get.get.nonEmpty){
//        print("Found")
//        return Future{ca.value.get.get.head.id.get}
//      }else {
//        print("Insert")
//        val c = (imports returning imports.map(_.id)) += methodDeclarationToAdd
//        val e: Future[Long] = DatabaseConnection.db.run(c)
//        Await.result(e,Duration.Inf);
//        Future{e.value.get.get}
//      }
    /**
      *
      *  val c = (imports returning imports.map(_.id))+= methodDeclarationToAdd
      * val futureResult :Future[Long] = DatabaseConnection.db.run(c)
      * futureResult
      */
//    val ca = MethodDeclaration.findByNameAndParam(methodDeclarationToAdd.methodName,methodDeclarationToAdd.params)
//    val cane = ca.map(x=>{
//      print("Cazzoooo")
//
//      if(x.nonEmpty){
//        print("Found")
//        return Future{x.head.id.get}
//      }else{
//        val c = (imports returning imports.map(_.id)) += methodDeclarationToAdd
//        val e = DatabaseConnection.db.run(c)
//        return e
//      }
//
//    })
//    cane
    //Await.result(ca,Duration.Inf)

  }

  def getAll:Future[Seq[MethodDeclaration]]= {
    val q = (for (c <- imports) yield c).result
    val c:Future[Seq[MethodDeclaration]]= DatabaseConnection.db.run(q)
    c
  }

  def insertIfNotExists(productInput: MethodDeclaration): Future[Long] = {

    val productAction = (
      imports.filter(x=> x.methodName===productInput.methodName && x.params===productInput.params).result.headOption.flatMap {
        case Some(product) =>
          DBIO.successful(product.id.get)

        case None =>
          (imports returning imports.map(_.id)) += productInput

      }
      ).transactionally
    DatabaseConnection.db.run(productAction)
  }

  def findByName(name:String):Future[Seq[MethodDeclaration]]={
    val q = MethodDeclaration.imports.filter(_.methodName === name).result
    DatabaseConnection.db.run(q)
  }
  def findByNameAndParam(name:String,param:Long):Future[Seq[MethodDeclaration]]={
    val q = MethodDeclaration.imports.filter(_.methodName === name).result
    DatabaseConnection.db.run(q)
  }

}

