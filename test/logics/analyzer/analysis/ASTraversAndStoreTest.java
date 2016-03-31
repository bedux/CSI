package logics.analyzer.analysis;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import exception.CustomException;
import junit.framework.TestCase;
import junit.framework.TestResult;
import logics.databaseUtilities.SaveClassAsTable;
import logics.models.db.JavaClass;
import logics.models.db.JavaFile;
import logics.models.db.JavaInterface;
import logics.models.query.QueryList;
import logics.models.query.QueryWithPath;
import org.h2.engine.Database;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;

/**
 * Created by bedux on 31/03/16.
 */
public class ASTraversAndStoreTest extends TestCase {
    @Before
    public void createDatabase() {

    }
   @Test
    public void testAST(){

       running(fakeApplication(), new Runnable() {
           public void run() {
               String filePath = "./test/logics/analyzer/analysis/Test1.java";
               try (
                       InputStream is = Files.newInputStream(new File(filePath).toPath())
               )

               {
                   CompilationUnit p = JavaParser.parse(is);
                   JavaFile test = new JavaFile();
                   test.path = filePath;
                   long id = new SaveClassAsTable().save(test);
                   MethodVisitorParameter mvh = new MethodVisitorParameter();
                   mvh.idFile = id;
                   new MethodVisitor().visit(p,mvh);
                   is.close();

                   List<JavaClass> listClass = QueryList.getInstance().getAllJavaClassByJavaFile(id);
                   assertEquals(listClass.size(), 4);

                   List<JavaInterface> listInterface = QueryList.getInstance().getAllJavaInterfaceByJavaFile(id);
                   assertEquals(listInterface.size(), 1);



               } catch (
                       Exception e
                       )

               {
                   throw new CustomException(e);
               }
           }
       });

   }
}