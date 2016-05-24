package database


import slick.driver.PostgresDriver.api._
import slick.lifted.TableQuery

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.Success
import scala.concurrent.ExecutionContext.Implicits.global


/** *
  *
  * @param id            Optional, Set as AutoIncrement in the database column definition
  * @param packageImport string contains the name of the package
  *
  */
case class ImportDeclaration(id: Option[Long], packageImport: String)

class ImportDeclarations(tag: Tag) extends Table[ImportDeclaration](tag, "import") {

  def id = column[Long]("id", O.AutoInc, O.PrimaryKey)

  def packageImport = column[String]("package")

  override def * = (id.?, packageImport) <>((ImportDeclaration.apply _).tupled, ImportDeclaration.unapply)

}

object ImportDeclaration {

  //TODO
  val imports = TableQuery[ImportDeclarations]


  private object Locker

  def insert(importDeclarationToAdd: ImportDeclaration): Future[Long] = {

    val productAction = (
      imports.filter(x=> x.packageImport===importDeclarationToAdd.packageImport).result.headOption.flatMap {
        case Some(product) =>
          DBIO.successful(product.id.get)

        case None =>
          (imports returning imports.map(_.id)) += importDeclarationToAdd
      }
      ).transactionally
    DatabaseConnection.db.run(productAction)


  }

  def getAll: Future[Seq[ImportDeclaration]] = {
    val q = (for (c <- imports) yield c).result
    val c: Future[Seq[ImportDeclaration]] = DatabaseConnection.db.run(q)
    c
  }

  def findByName(name: String): Future[Seq[ImportDeclaration]] = {
    val q = ImportDeclaration.imports.filter(_.packageImport === name).result
    DatabaseConnection.db.run(q)
  }

}

