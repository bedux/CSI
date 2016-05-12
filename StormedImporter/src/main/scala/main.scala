/**
 * Created by bedux on 02/04/16.
 */
package database
import ch.usi.inf.reveal.parsing.artifact._
import ch.usi.inf.reveal.parsing.model.CommentNode
import ch.usi.inf.reveal.parsing.model.java.{TypeNode, PrimitiveTypeNode, ImportDeclarationNode, IdentifierNode}
import ch.usi.inf.reveal.parsing.model.visitors.{MethodDeclaratorNodeVisitor, ImportDeclarationNodeVisitor,MethodInvocationNodeVisitor}
import ch.usi.inf.reveal.parsing.units.MetaInformation
import com.sun.org.apache.xalan.internal.utils.FeatureManager.Feature
import sun.reflect.generics.tree.ReturnType
import sun.reflect.generics.visitor.Visitor
import scala.concurrent.{ExecutionContext, Future, Await}
import scala.concurrent.duration.Duration
import ExecutionContext.Implicits.global

import scala.util.{Failure, Success, Try}
import scala.reflect.runtime.universe._
import database._


case class PSQLException(msg:String) extends Exception

case class Utils(jsonFilePath:String) {
  import HASTNodeImplicits._

  def getFuture = Future {
    println("Starting " + jsonFilePath)


    val artifact = ArtifactSerializer.deserializeFromFile(jsonFilePath)


    /** *
      * create the discussion in the database
      */
    val discussion = DiscussionUtils.insert(Discussion(None, jsonFilePath))
//    Await.result(discussion, Duration.Inf)

    val storeImportInformation: Future[Seq[(Long,Long)]] = discussion.flatMap{ id =>
      /** *
        * Getting list of all imports of this discussion
        */
      val listVisitor = ImportDeclarationNodeVisitor.list()
      val listImportNodes = listVisitor(List(), artifact)
      val listImports = listImportNodes.map {
        _.asStringName
      }


      /** *
        * Inserting into the database
        */
      val storeImportInformation: Seq[Future[Long]] = listImports.map { importDeclId => ImportDeclaration.insert(ImportDeclaration(None, importDeclId)) }
      val res:  Seq[Future[(Long,Long)]] = storeImportInformation.map(_.flatMap(entryId => ImportDiscussion.insert(ImportDiscussion(Some(id), Some(entryId)))))
      Future.sequence(res)
    }



//    val id = discussion.value.get match {
//      case Success(x) => x
//      case Failure(e) => throw e
//    }
//
    val storeMethodInformation: Future[Seq[(Long,Long)]] = discussion.flatMap{ id =>
      /** *
        * Getting list of all imports of this discussion
        */
      val listVisitor = MethodInvocationNodeVisitor.list()
      val listMethodsNodes = listVisitor(List(), artifact)
      val listMethods = listMethodsNodes.map {
        x=>(x.identifier.name,x.args.arguments.length)
      }.distinct


      /** *
        * Inserting into the database
        */
      val storeImportInformation: Seq[Future[Long]] = listMethods.map { MethodInfo => MethodDeclaration.insert(MethodDeclaration(None, MethodInfo._1, MethodInfo._2)) }
      val res:  Seq[Future[(Long,Long)]] = storeImportInformation.map(_.flatMap(entryId => MethodDiscussion.insert(MethodDiscussion(Some(id), Some(entryId)))))
      Future.sequence(res)
    }






//    val listMethod = MethodDeclaratorNodeVisitor.list()
//    val collected = listMethod(List(), artifact)
//    val returnTypeList: Seq[String] = collected.map(x => x.returnType.getOrElse(PrimitiveTypeNode("void", Seq[CommentNode]())).name + x.identifier.name)
//    println(returnTypeList)


    /** *
      * Wait for the compotation before return
      */
//    Await.result(Future.sequence(storeImportInformation.toList),Duration.Inf)

      Future.sequence(Seq(storeImportInformation,storeMethodInformation))
  }

}



object HASTNodeImplicits {

  implicit class ImportDeclarationNodeExtension(node: ImportDeclarationNode) {

    def asStringName = {
      val allNames = node.identifier.identifiers.map{_.name}
      allNames.mkString(".") + (if(node.isOnDemand) ".*" else "")
    }

  }

}


object main extends App{


    val filesHere = (new java.io.File(args(0))).listFiles
    val futureList = filesHere.map(x=>Utils(x.getAbsolutePath).getFuture)
    val futur: Future[List[Future[Seq[Seq[(Long, Long)]]]]] = Future.sequence(futureList.toList);
    futur.onFailure({
      case e => throw e
    })
    Await.result(futur,Duration.Inf)



}
