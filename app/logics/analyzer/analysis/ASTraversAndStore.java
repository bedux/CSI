package logics.analyzer.analysis;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import exception.CustomException;
import exception.SQLnoResult;
import interfaces.Analyser;
import interfaces.Component;
import logics.ExtensionTool;
import logics.analyzer.DataFile;
import logics.databaseUtilities.SaveClassAsTable;
import logics.models.db.*;
import logics.models.db.information.*;
import logics.models.query.QueryList;
import play.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Created by bedux on 25/03/16.
 */
public class ASTraversAndStore implements Analyser< CompletableFuture<Integer>> {

    /***
     * Given a component, run the analysis on his child and, if is a javaFile, it compute all the characteristic using the AST
     *
     * @param value component to be analyzed
     * @return NOt important!Just a random number
     */
    @Override
    public  CompletableFuture<Integer> analysis(Component value) {
//        value.getComponentList().stream().forEach((x) -> x.applyFunction((new ASTraversAndStore())::analysis));
//        if (value instanceof DataFile) {
//            analysisDataFile((DataFile) value);
//        }
//        return 1;
        CompletableFuture[] res =
                value.getComponentList().stream().map(
                        (x) -> CompletableFuture.supplyAsync(() -> x.applyFunction((new ASTraversAndStore())::analysis))
                ).toArray(CompletableFuture[]::new);

        CompletableFuture<Void> allDoneFuture = CompletableFuture.allOf(res);
        allDoneFuture.join();
        return allDoneFuture.thenApply(
                v -> {
                    if (value instanceof DataFile) {
                        analysisDataFile((DataFile) value);
                        return 1;
                    } else {
                        return 1;
                    }
                }
        );
    }


    /***
     * Run the analise overe DataFile component
     *
     * @param c the data file
     */
    private void analysisDataFile(DataFile c) {
        ExtensionTool currentPath = new ExtensionTool(c.getFeatures().getFilePath(), c.getFeatures().getPath());
        if (currentPath.isJava()) {
            analyseJavaFile(currentPath);
        } else {
            try {


                TextFile analyzedFile =  QueryList.getInstance().getTextFileByPath(currentPath.getPath()).orElseThrow(()->new SQLnoResult());
                if (analyzedFile.json == null) {
                    analyzedFile.json = new JavaFileInformation();
                }
                Long l = Files.size(c.getFeatures().getFilePath());
                if (l != null) {
                    analyzedFile.json.size = l.intValue();
                    new SaveClassAsTable().update(analyzedFile);
                }
            } catch (Exception e) {
                new CustomException(e);

            }


        }

    }


    /***
     * @param currentPath the java file to be analyses
     */
    private void analyseJavaFile(ExtensionTool currentPath) {
        JavaFile analyzedFile;
        Logger.info("Analyse "+currentPath.getPath());
        try {
            analyzedFile = QueryList.getInstance().getJavaFileByPath(currentPath.getPath()).orElseThrow(() -> new SQLnoResult());
            Long l = Files.size(currentPath.getFilePath());
            if (l != null) {
                analyzedFile.json.size = l.intValue();
                new SaveClassAsTable().update(analyzedFile);
            }
        } catch (Exception e) {
            throw new CustomException(e);
        }
        try (InputStream is = Files.newInputStream(currentPath.getFilePath())) {
            CompilationUnit p = JavaParser.parse(is);
            analyzedFile.json.noLine = p.getEndLine() - p.getBeginLine();
            new SaveClassAsTable().update(analyzedFile);
            MethodVisitorParameter mvh = new MethodVisitorParameter();
            mvh.idFile = analyzedFile.id;
            new MethodVisitor().visit(p, mvh);
            is.close();

        } catch (IOException e) {
            new CustomException(e);
        } catch (SQLException e) {
            new CustomException(e);
        } catch (InstantiationException e) {
            new CustomException(e);
        } catch (ParseException e) {
            new CustomException(e);
        } catch (IllegalAccessException e) {
            new CustomException(e);
        }

    }
}

/***
 * Only a field class for travers the AST
 */
class MethodVisitorParameter{
    public long idFile;
    public long idJavaSource = -1;
    public long idJavaDoc;
    public long javaMethodId=-1;
    public long countJavaMethod= 0;
    public boolean isInterface  = false;
}


class MethodVisitor extends VoidVisitorAdapter<MethodVisitorParameter> {


    private String clear(String s) {
        return s.replaceAll("[\u0000]", "");
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
    public void visit(MethodCallExpr n, MethodVisitorParameter arg) {
        try {
            if(arg.javaMethodId==-1 ){
                if(arg.idJavaSource!=-1) {
                    if (arg.isInterface) {
                        JavaInterface jm = new SaveClassAsTable().get(arg.idJavaSource, JavaInterface.class).orElseThrow(() -> new SQLnoResult());
                        jm.json.variableDeclaration.add(n.getName());
                        new SaveClassAsTable().update(jm);

                    } else {
                        JavaClass jm = new SaveClassAsTable().get(arg.idJavaSource, JavaClass.class).orElseThrow(() -> new SQLnoResult());
                        jm.json.variableDeclaration.add(n.getName());
                        new SaveClassAsTable().update(jm);
                    }
                }

            }else {
                JavaMethod jm = new SaveClassAsTable().get(arg.javaMethodId, JavaMethod.class).orElseThrow(() -> new SQLnoResult());
                if (jm.json.variableDeclaration == null) {
                    jm.json.variableDeclaration = new ArrayList<>();
                }
                jm.json.variableDeclaration.add(n.getName());
                new SaveClassAsTable().update(jm);
            }

        } catch (SQLException e) {
            throw new CustomException(e);
        } catch (InstantiationException e) {
            throw new CustomException(e);
        } catch (IllegalAccessException e) {
            throw new CustomException(e);
        }


        //.updateJsonField("JavaMethod", "Information", "{variableDeclaration," + arg.countJavaMethod + "}", n.getName(), arg.javaMethodId);
        arg.countJavaMethod++;
        super.visit(n, arg);

    }

    @Override
    public void visit(MethodDeclaration n, MethodVisitorParameter arg) {

        MethodInfoJSON thisInfo = new MethodInfoJSON();
        thisInfo.modifier = clear(getModifierAsString(n.getModifiers()));
        thisInfo.lineEnd = n.getEndLine();
        thisInfo.lineStart = n.getBeginLine();
        thisInfo.signature = clear(n.getName());
        JavaMethod method = new JavaMethod();
        method.javaSource = arg.idJavaSource;
        method.json = thisInfo;
        arg.idJavaDoc = new SaveClassAsTable().save(method);
        arg.javaMethodId =  arg.idJavaDoc;
        super.visit(n, arg);

    }

    @Override
    public void visit(FieldDeclaration n, MethodVisitorParameter o) {

        FieldsInfoJSON info = new FieldsInfoJSON();

        info.type = clear(n.getType().toString());
        info.modifier = getModifierAsString(n.getModifiers());
        info.name =   n.getVariables().get(0).getId().getName();
        JavaField jf = new JavaField();
        jf.javaSource = o.idJavaSource;
        jf.json = info;
        o.idJavaDoc = new SaveClassAsTable().save(jf);


        super.visit(n, o);
    }

    @Override
    public void visit(ImportDeclaration n, MethodVisitorParameter arg) {
        JavaImport javaImport = new JavaImport();
        javaImport.json = new JavaImportInformation();
        javaImport.json.isAsterisk = n.isAsterisk();
        javaImport.json.isStatic = n.isStatic();
        javaImport.javaFile = arg.idFile;
        javaImport.json.name = n.getName().toString();
        long id = new SaveClassAsTable().save(javaImport);
        super.visit(n, arg);


    }

    @Override
    public void visit(JavadocComment n, MethodVisitorParameter e) {
        if(!n.isLineComment() && e.idJavaDoc!=-1) {
            JavaDocIfoJSON info = new JavaDocIfoJSON();
            info.text = clear(n.toStringWithoutComments());
            JavaDoc jdc = new JavaDoc();
            jdc.json = info;
            jdc.containsTransverseInformation = e.idJavaDoc;
            e.idJavaDoc = -1;
            long id = new SaveClassAsTable().save(jdc);
        }
        super.visit(n, e);


    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, MethodVisitorParameter e) {
        MethodInfoJSON thisInfo = new MethodInfoJSON();
        thisInfo.modifier = "PUBLIC";
        thisInfo.lineEnd = n.getEndLine();
        thisInfo.lineStart = n.getBeginLine();
        thisInfo.signature = clear(n.getName());
        MethodVisitorParameter newId = new MethodVisitorParameter();
        newId.idFile = e.idFile;

        if (n.isInterface()) {
            JavaInterface method = new JavaInterface();
            method.javaFile = e.idFile;
            method.json = thisInfo;
            newId.idJavaSource = new SaveClassAsTable().save(method);
            newId.idJavaDoc =  newId.idJavaSource;
            newId.isInterface = true;
        } else {
            JavaClass method = new JavaClass();
            method.javaFile = e.idFile;
            method.json = thisInfo;
            newId.idJavaSource = new SaveClassAsTable().save(method);
            newId.idJavaDoc =  newId.idJavaSource;
        }
        super.visit(n, newId);


    }

    @Override
    public void visit(ConstructorDeclaration n, MethodVisitorParameter arg) {
        MethodInfoJSON thisInfo = new MethodInfoJSON();
        thisInfo.modifier = clear(getModifierAsString(n.getModifiers()));
        thisInfo.lineEnd = n.getEndLine();
        thisInfo.lineStart = n.getBeginLine();
        thisInfo.signature = clear(n.getName());
        JavaMethod method = new JavaMethod();
        method.javaSource = arg.idJavaSource;
        method.json = thisInfo;
        arg.idJavaDoc = new SaveClassAsTable().save(method);
        arg.javaMethodId =  arg.idJavaDoc;

        super.visit(n, arg);
    }

    @Override
    public void visit(EnumDeclaration n, MethodVisitorParameter arg) {

        JavaEnum javaEnum =  new JavaEnum();
        javaEnum.javaFile = arg.idFile;
        javaEnum.json = new JavaEnumInformation();
        javaEnum.json.name = n.toString();
        new SaveClassAsTable().save(javaEnum);
        super.visit(n, arg);


    }

    @Override
    public void visit(EnumConstantDeclaration n, MethodVisitorParameter arg) {

        JavaEnum javaEnum =  new JavaEnum();
        javaEnum.javaFile = arg.idFile;
        javaEnum.json = new JavaEnumInformation();
        javaEnum.json.name = n.toString();
        new SaveClassAsTable().save(javaEnum);
        super.visit(n, arg);


    }

    @Override
    public void visit(PackageDeclaration n, MethodVisitorParameter arg) {
        JavaPackage javaPackage = new JavaPackage();
        javaPackage.javaFile = arg.idFile;
        javaPackage.json = new JavaPackageInformation();
        javaPackage.json.name = n.getName().toStringWithoutComments();
        new SaveClassAsTable().save(javaPackage);
        super.visit(n, arg);


    }

    @Override
    public void visit(VariableDeclarationExpr n, MethodVisitorParameter arg) {
        super.visit(n, arg);

    }




}

