package logics.analyzer.analysis;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.ClassExpr;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.ForeachStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import exception.CustomException;
import interfaces.Analyser;
import interfaces.Component;
import logics.Definitions;
import logics.analyzer.BinaryFile;
import logics.analyzer.DataFile;
import logics.databaseUtilities.SaveClassAsTable;
import logics.models.db.*;
import logics.models.query.QueryList;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.sql.SQLException;

/**
 * Created by bedux on 25/03/16.
 */
public class ASTraversAndStore implements Analyser<Integer> {
    @Override
    public Integer analysis(Component value) {
        value.getComponentList().stream().forEach((x) -> x.applyFunction((new ASTraversAndStore())::analysis));
        if (value instanceof BinaryFile) {

        } else if (value instanceof DataFile) {
            analysisCast((DataFile) value);
        }
        return 1;
    }


    private void analysisCast(DataFile c) {
        String fn = c.getFeatures().getPath().substring(c.getFeatures().getPath().lastIndexOf(".") + 1);

        String filePath = c.getFeatures().getPath();
        if (fn.indexOf("java") == 0) {


            JavaFile analyzedFile;


            try {
                analyzedFile = QueryList.getInstance().getJavaFileByPath(filePath);
                Long l =  Files.size(c.getFeatures().getFilePath());
                if(l!=null) {
                    analyzedFile.json.size = l.intValue();
                    new SaveClassAsTable().update(analyzedFile);
                }

            } catch (Exception  e) {
                throw new CustomException(e);

            }
            try (InputStream is = Files.newInputStream(c.getFeatures().getFilePath())) {
                CompilationUnit p = JavaParser.parse(is);
                analyzedFile.json.noLine = p.getEndLine() - p.getBeginLine();
                new SaveClassAsTable().update(analyzedFile);
                new MethodVisitor().visit(p, analyzedFile.id);
                is.close();

            } catch (Exception e) {
                 new CustomException(e);
            }
        }else{
            try {
                TextFile analyzedFile = QueryList.getInstance().getTextFileByPath(filePath);
                if (analyzedFile.json == null) {
                    analyzedFile.json = new JavaFileInformation();
                }
                Long l =  Files.size(c.getFeatures().getFilePath());;
                if(l!=null) {
                    analyzedFile.json.size = l.intValue();
                    new SaveClassAsTable().update(analyzedFile);
                }
            } catch (Exception  e) {
                 new CustomException(e);

            }


        }

    }


}

class MethodVisitor extends VoidVisitorAdapter<Long> {


    private String clear(String s) {
        return s.replaceAll("[\u0000-\uFFFF]", "");
    }

    private String getModifierAsString(int m) {
        if (Modifier.isPrivate(m)) {
            return "PRIVATE";
        } else if (Modifier.isPublic(m)) {
            return "PUBLIC";
        } else if (Modifier.isProtected(m)) {
            return "PROTECTED";
        }
        return "UNDEF";
    }


    @Override
    public void visit(MethodDeclaration n, Long arg) {

        MethodInfoJSON thisInfo = new MethodInfoJSON();
        thisInfo.modifier = clear(getModifierAsString(n.getModifiers()));
        thisInfo.lineEnd = n.getEndLine();
        thisInfo.lineStart = n.getBeginLine();
        thisInfo.signature = clear(n.getName());
        JavaMethod method = new JavaMethod();
        method.javaSource = arg;
        method.json = thisInfo;
        long newId = new SaveClassAsTable().save(method);
        super.visit(n, newId);

    }

    @Override
    public void visit(FieldDeclaration n, Long o) {

        FieldsInfoJSON info = new FieldsInfoJSON();

        info.type = clear(n.getType().toString());
        info.modifier = getModifierAsString(n.getModifiers());
        info.name =   n.getVariables().get(0).getId().getName();
        JavaField jf = new JavaField();
        jf.javaSource = o;
        jf.json = info;
        long newId =  new SaveClassAsTable().save(jf);
        super.visit(n, newId);
    }

    @Override
    public void visit(ForeachStmt n, Long o) {

        super.visit(n, o);

//        o.setNoForeachSTM(o.getNoForeachSTM()+1);


    }

    @Override
    public void visit(ForStmt n, Long o) {
        super.visit(n, o);
//        o.setNoForSTM(o.getNoForSTM()+1);


    }

    @Override
    public void visit(WhileStmt n, Long o) {
        super.visit(n, o);
//        o.setNoWhile(o.getNoWhile() + 1);

    }

    @Override
    public void visit(IfStmt n, Long o) {
        super.visit(n, o);
//        o.setNoIf(o.getNoIf()+1);

    }

    @Override
    public void visit(JavadocComment n, Long e) {
        JavaDocIfoJSON info = new JavaDocIfoJSON();
        info.text = clear(n.toStringWithoutComments());
        JavaDoc jdc = new JavaDoc();
        jdc.json = info;
        jdc.containsTransverseInformation = e;
        new SaveClassAsTable().save(jdc);
        super.visit(n, e);


    }



    @Override
    public void visit(ClassOrInterfaceDeclaration n, Long e) {
        MethodInfoJSON thisInfo = new MethodInfoJSON();
        thisInfo.modifier = "PUBLIC";
        thisInfo.lineEnd = n.getEndLine();
        thisInfo.lineStart = n.getBeginLine();
        thisInfo.signature = clear(n.getName());
        long newId = 0;
        if (n.isInterface()) {
            JavaInterface method = new JavaInterface();
            method.javaFile = e;
            method.json = thisInfo;
            newId = new SaveClassAsTable().save(method);
        } else {
            JavaClass method = new JavaClass();
            method.javaFile = e;
            method.json = thisInfo;
            newId = new SaveClassAsTable().save(method);
        }
        super.visit(n, newId);


    }


}


//class ASTHelperPreviews{
//    JavaSourceObject currentContext;
//}
