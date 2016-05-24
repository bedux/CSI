package logics.analyzer.analysis;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Transaction;
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
import logics.analyzer.UsedData;
import logics.models.modelQuery.Query;
import logics.models.newDatabase.*;
import logics.versionUtils.WebSocketProgress;
import play.Logger;
import play.db.ebean.Model;

import java.io.*;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Created by bedux on 25/03/16.
 */
public class ASTraversAndStore implements Analyser< CompletableFuture<Integer>> {

    WebSocketProgress webSocketProgress= null;



    public ASTraversAndStore(){




    }
    public ASTraversAndStore(WebSocketProgress webSocketProgress){
        this.webSocketProgress = webSocketProgress;
    }

    /***
     * Given a component, run the analysis on his child and, if is a javaFile, it compute all the characteristic using the AST
     *
     * @param value component to be analyzed
     * @return NOt important!Just a random number
     */
    @Override
    public  CompletableFuture<Integer> analysis(Component value) {

        List<CompletableFuture> list = value.getComponentList().stream().map(
                (x -> x.applyFunction((new ASTraversAndStore())::analysis)))
                .collect(Collectors.toList());


        return  CompletableFuture.allOf(list.toArray(new CompletableFuture[list.size()]))
                .thenApplyAsync((x)->{
                            if (value instanceof DataFile) {
                                analysisDataFile((DataFile) value);
                            }
                            return 1;
                        }
                        ,ThreadManager.instance().getExecutor());




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
                TextFile analyzedFile =  (TextFile)TextFile.find.where().eq("name", c.getFeatures().getPath()).findList().get(0);
                Long l = Files.size(c.getFeatures().getFilePath());
                if (l != null) {
                    analyzedFile.size = l;
                    analyzedFile.save();                }
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
        Logger.info("Analyse "+currentPath.getPath()+" Thread id=>"+Thread.currentThread().getId());

        try {
            Query q = new Query();
            analyzedFile = q.JavaFileByPath(currentPath.getPath()).orElseThrow(() -> new SQLnoResult());
            Long l = Files.size(currentPath.getFilePath());
            if (l != null) {

                analyzedFile.size = l;
                try(Stream<String> stream = Files.lines(currentPath.getFilePath())) {
                    analyzedFile.nline = stream.count();
                }
                analyzedFile.update();




            }
        } catch (Exception e) {
            throw new CustomException(e);
        }
        try (InputStream is = Files.newInputStream(currentPath.getFilePath())) {
            CompilationUnit p = JavaParser.parse(is);


            MethodVisitorParameter mvh = new MethodVisitorParameter();
            mvh.refJavaFile = analyzedFile;
            new MethodVisitor().visit(p, mvh);
            is.close();
            Ebean.save(mvh.toBeSaved);
            analyzedFile.update();

        } catch (IOException e) {
            throw new CustomException(e);
        }  catch (ParseException e) {
            return;

        }catch (Exception e){
            throw new CustomException(e);
        }


    }
}

/***
 * Only a field class for travers the AST
 */
class MethodVisitorParameter{
    public JavaFile refJavaFile ;
    public int idJavaDoc;

    public List<Model> toBeSaved = new ArrayList<>();

}


class MethodVisitor extends VoidVisitorAdapter<MethodVisitorParameter> {


    private String clear(String s) {
        return s.replaceAll("[\u0000]", "");
    }

    private String getModifierAsString
            (int m) {
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

       // List<JavaMethodCall> result = JavaMethodCall.find.where().eq("methodname",n.getName()).eq("params",n.getArgs().size()).findList();

        Optional<JavaMethodCall> result = UsedData.getInstance().getMethodCall(n.getName().toString(),n.getArgs().size());


        JavaMethodCall jmc;
        if(!result.isPresent()){
            jmc = new JavaMethodCall();
            jmc.methodname = n.getName();
            jmc.params = n.getArgs().size();

            MethodFile mf = new MethodFile();
//            arg.refJavaFile.methodFileList.add(mf);
            mf.javaMethodCall = jmc;
//            jmc.methodFileList.add(mf);
            mf.javaFile = arg.refJavaFile;
            jmc.save();
            mf.save();
            UsedData.getInstance().addMethodCall(jmc);

//            Ebean.update(arg.refJavaFile);

        }else{
            jmc = result.get();
            MethodFile mf = new MethodFile();
//            arg.refJavaFile.methodFileList.add(mf);
            mf.javaMethodCall = jmc;
//            jmc.methodFileList.add(mf);
            mf.javaFile = arg.refJavaFile;

            mf.save();
//            Ebean.update(arg.refJavaFile);
        }






        super.visit(n, arg);

    }

    @Override
    public void visit(MethodDeclaration n, MethodVisitorParameter arg) {

        JavaMethod jm = new JavaMethod();
        jm.javaFile = arg.refJavaFile;
        jm.name = n.getName();
        arg.toBeSaved.add(jm);
        arg.idJavaDoc = 1;

        super.visit(n, arg);
    }


    @Override
    public void visit(AnnotationDeclaration n, MethodVisitorParameter e) {

        JavaInterface ji = new JavaInterface();
        ji.javaFile = e.refJavaFile;
        ji.name = n.getName();
        e.toBeSaved.add(ji);

        super.visit(n,e);
    }


    @Override
    public void visit(FieldDeclaration n, MethodVisitorParameter o) {

        JavaField jf = new JavaField();
        jf.javaFile =  o.refJavaFile;
        jf.name = n.getVariables().get(0).getId().getName();

        o.toBeSaved.add(jf);

        super.visit(n, o);
    }

    @Override
    public void visit(ImportDeclaration n, MethodVisitorParameter arg) {

         final String importName = n.getName().toString() + ((n.isAsterisk())?".*":"");

        Optional<JavaImport> result = UsedData.getInstance().getImport(importName);

        JavaImport jmc;
        if(!result.isPresent()){
            jmc = new JavaImport();
            jmc.packageName = importName;


            ImportFile mf = new ImportFile();
            mf.javaImport = jmc;
            mf.javaFile = arg.refJavaFile;


            jmc.save();
            mf.save();

            UsedData.getInstance().addImport(jmc);


        }else{
            jmc = result.get();
            ImportFile mf = new ImportFile();
            mf.javaImport = jmc;
            mf.javaFile = arg.refJavaFile;

            mf.save();
        }

        super.visit(n, arg);


    }

    @Override
    public void visit(JavadocComment n, MethodVisitorParameter e) {

            if(e.idJavaDoc==1) {
                JavaDoc jc = new JavaDoc();
                jc.javaFile = e.refJavaFile;
                //e.refJavaFile.javaDocList.add(jc);
                jc.name = clear(n.toStringWithoutComments());
                e.toBeSaved.add(jc);
                e.idJavaDoc=0;
            }

        super.visit(n, e);


    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, MethodVisitorParameter e) {


        if(n.isInterface()){
            JavaInterface ji = new JavaInterface();
            ji.name = n.getName();
            ji.javaFile = e.refJavaFile;
            //e.refJavaFile.javaInterfaceList.add(ji);

            e.toBeSaved.add(ji);
        }else{
            JavaClass ji = new JavaClass();
            ji.name = n.getName();
            ji.javaFile = e.refJavaFile;
            //e.refJavaFile.javaClassList.add(ji);
            e.toBeSaved.add(ji);
        }

        super.visit(n, e);

    }

    @Override
    public void visit(ConstructorDeclaration n, MethodVisitorParameter arg) {

        JavaMethod jm = new JavaMethod();
        jm.javaFile =  arg.refJavaFile;
       // arg.refJavaFile.javaMethodList.add(jm);
        jm.name = n.getName();
        arg.toBeSaved.add(jm);
        arg.idJavaDoc=1;
        super.visit(n, arg);

    }


    @Override
    public void visit(PackageDeclaration n, MethodVisitorParameter arg) {


        JavaPackage jp = new JavaPackage();
        jp.javaFile = arg.refJavaFile;
        //arg.refJavaFile.javaPackageList.add(jp);


        jp.name = n.getName().toString();
        arg.toBeSaved.add(jp);
        super.visit(n, arg);


    }




}

