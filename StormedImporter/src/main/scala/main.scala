/**
  * Created by bedux on 02/04/16.
  */
package database

import java.util.concurrent.Executors

import ch.usi.inf.reveal.parsing.artifact._
import ch.usi.inf.reveal.parsing.model.java.ImportDeclarationNode
import ch.usi.inf.reveal.parsing.model.visitors.{ImportDeclarationNodeVisitor, MethodInvocationNodeVisitor}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.ExecutionContext.Implicits.global

case class PSQLException(msg: String) extends Exception

case class Utils(jsonFilePath: String) {

  import HASTNodeImplicits._


  def getFuture:Future[Seq[(Long,Long)]] =  {
    val artifact = ArtifactSerializer.deserializeFromFile (jsonFilePath)

    /** *
      * create the discussion in the database
      */

    val seqF: Future[Seq[(Long, Long)]] = for {

    discussion <- {
    println(jsonFilePath)
    DiscussionUtils.insert (Discussion (None, jsonFilePath) )

  }

    storeImp <- {

    /** *
      * Getting list of all imports of this discussion
      */
    val listVisitor = ImportDeclarationNodeVisitor.list ()
    val listImportNodes = listVisitor (List (), artifact)
    val listImports = listImportNodes.map {
    _.asStringName
  }.distinct
    val o: Seq[(Long, Long)]= listImports.map {
    importDeclId => val c = {
    for {
    rend <- ImportDeclaration.insert (ImportDeclaration (None, importDeclId) )
    end <- ImportDiscussion.insert (ImportDiscussion (Some (discussion), Some (rend) ) )
  } yield end
  }
    Await.result (c, Duration.Inf)
    c.value.get.get
  }
    Future {o}
    //Future.sequence(o)
  }

    storMeth <- {
    val listVisitor = MethodInvocationNodeVisitor.list ()
    val listMethodsNodes = listVisitor (List (), artifact)
    val listMethods = listMethodsNodes.map {
    x => (x.identifier.name, x.args.arguments.length)
  }.distinct
    val o:  Seq[(Long, Long)] = listMethods.map (x => (discussion, x) ).distinct.map {importDeclId => {
    val c = for {
                  rend <- MethodDeclaration.insert (MethodDeclaration (None, importDeclId._2._1, importDeclId._2._2) )
                  end <- MethodDiscussion.insert (MethodDiscussion (Some (discussion), Some (rend) ) )
                } yield end
    Await.result (c, Duration.Inf)
    c.value.get.get
  }
  }
    Future {o}

  }
    s <- Future {Seq (storMeth, storeImp).flatMap(x=>x)}
  } yield s
    seqF

  }


}


object HASTNodeImplicits {

  implicit class ImportDeclarationNodeExtension(node: ImportDeclarationNode) {

    def asStringName = {
      val allNames = node.identifier.identifiers.map {
        _.name
      }
      allNames.mkString(".") + (if (node.isOnDemand) ".*" else "")
    }

  }

}


object main extends App {


  val filesHere = (new java.io.File(args(0))).listFiles
//  filesHere.foreach(x => {
//    val toBe = Utils(x.getAbsolutePath).getFuture
//    Await.result(toBe, Duration.Inf);
//
//  })



  val futureList:Array[Future[Seq[(Long,Long)]]] = filesHere.map(x=>Utils(x.getAbsolutePath).getFuture)
  val futSew :Seq[Future[Seq[(Long,Long)]]] = futureList.toList;
  val futur:Future[Seq[Seq[(Long,Long)]]] = Future.sequence(futSew)
  futur.onFailure({
    case e => throw e
  })
  Await.result(futur,Duration.Inf)



}
