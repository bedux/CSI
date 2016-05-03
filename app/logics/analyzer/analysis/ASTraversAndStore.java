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
import logics.databaseCache.DatabaseModels;
import logics.models.db.*;
import logics.models.db.information.*;
import logics.models.modelQuery.Query;
import play.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


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

                TextFile analyzedFile =   Query.TextFileByPath(currentPath.getPath()).orElseThrow(()->new SQLnoResult());
                if (analyzedFile.getJson() == null) {
                    analyzedFile.setJson( new JavaFileInformation());
                }
                Long l = Files.size(c.getFeatures().getFilePath());
                if (l != null) {
                    JavaFileInformation jfi =  analyzedFile.getJson();
                    jfi.size = l.intValue();
                    analyzedFile.setJson(jfi);
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
        Logger.info("Analyse "+currentPath.getPath()+" Thread id=>"+Thread.currentThread().getId());
        try {
            analyzedFile = Query.JavaFileByPath(currentPath.getPath()).orElseThrow(() -> new SQLnoResult());
            Long l = Files.size(currentPath.getFilePath());
            if (l != null) {
                JavaFileInformation jfi =  analyzedFile.getJson();
                jfi.size = l.intValue();
                analyzedFile.setJson(jfi);
            }
        } catch (Exception e) {
            throw new CustomException(e);
        }
        try (InputStream is = Files.newInputStream(currentPath.getFilePath())) {
            CompilationUnit p = JavaParser.parse(is);

            JavaFileInformation jfi =  analyzedFile.getJson();
            jfi.noLine = p.getEndLine() - p.getBeginLine();
            analyzedFile.setJson(jfi);
            MethodVisitorParameter mvh = new MethodVisitorParameter();
            mvh.idFile = analyzedFile.getId();
            new MethodVisitor().visit(p, mvh);
            is.close();

        } catch (IOException e) {
            new CustomException(e);
        }  catch (ParseException e) {
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
    public boolean isEnum  = false;

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

            if(arg.javaMethodId==-1 ){
                if(arg.idJavaSource!=-1) {
                    if (arg.isInterface) {
                        JavaInterface jm =DatabaseModels.getInstance().getEntity(JavaInterface.class,arg.idJavaSource).orElseThrow(() -> new SQLnoResult());
                        MethodInfoJSON mif = jm.getJson();
                        mif.variableDeclaration.add(n.getName());
                        jm.setJson(mif);

                    } else if(!arg.isEnum){
                        JavaClass jm = DatabaseModels.getInstance().getEntity(JavaClass.class,arg.idJavaSource).orElseThrow(() -> new SQLnoResult());
                        MethodInfoJSON mif = jm.getJson();
                        mif.variableDeclaration.add(n.getName());
                        jm.setJson(mif);
                    }
                }

            }else {
                JavaMethod jm =DatabaseModels.getInstance().getEntity(JavaMethod.class,arg.javaMethodId).orElseThrow(() -> new SQLnoResult());
                if (jm.getJson().variableDeclaration == null) {
                    MethodInfoJSON mif = jm.getJson();
                    mif.variableDeclaration=new ArrayList<>();
                    jm.setJson(mif);
                }
                MethodInfoJSON mif = jm.getJson();
                mif.variableDeclaration.add(n.getName());
                jm.setJson(mif);
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


        JavaMethod method =  DatabaseModels.getInstance().getEntity(JavaMethod.class).get();
        method.setJson(thisInfo);

        Optional<JavaClass> jsc =  DatabaseModels.getInstance().getEntity(JavaClass.class, arg.idJavaSource);
        Optional<JavaInterface> jsi =  DatabaseModels.getInstance().getEntity(JavaInterface.class, arg.idJavaSource);
        Optional<JavaEnum> jse =  DatabaseModels.getInstance().getEntity(JavaEnum.class, arg.idJavaSource);

        if(jsc.isPresent()){
            jsc.get().getListOfMethod();
            jsc.get().addlistOfMethod(method);
        }
        else if(jsi.isPresent()){

            jsi.get().getListOfMethod();
            jsi.get().addlistOfMethod(method);
        }else if(jse.isPresent()){

            jse.get().getListOfMethod();
            jse.get().addlistOfMethod(method);
        }
        else{
            Logger.error("Not exhaustive pattern");

        }


//        jsp.getListOfMethod();
//        jsp.addlistOfMethod(method);


        arg.idJavaDoc = method.getId();
        arg.javaMethodId =  arg.idJavaDoc;
        super.visit(n, arg);

    }


    @Override
    public void visit(AnnotationDeclaration n, MethodVisitorParameter e) {

        MethodInfoJSON thisInfo = new MethodInfoJSON();
        thisInfo.modifier = "PUBLIC";
        thisInfo.lineEnd = n.getEndLine();
        thisInfo.lineStart = n.getBeginLine();
        thisInfo.signature = clear(n.getName());

        JavaInterface method = DatabaseModels.getInstance().getEntity(JavaInterface.class).get();
        JavaFile jf = DatabaseModels.getInstance().getEntity(JavaFile.class,e.idFile).get();
        jf.getListOfJavaInterface();
        jf.addListOfJavaInterface(method);
        method.setJson(thisInfo);

        MethodVisitorParameter newId = new MethodVisitorParameter();
        newId.idFile = e.idFile;
        newId.idJavaSource =method.getId();
        newId.idJavaDoc =  newId.idJavaSource;
        newId.isInterface = true;
    }


    @Override
    public void visit(FieldDeclaration n, MethodVisitorParameter o) {

        FieldsInfoJSON info = new FieldsInfoJSON();
        info.type = clear(n.getType().toString());
        info.modifier = getModifierAsString(n.getModifiers());
        info.name =   n.getVariables().get(0).getId().getName();

        JavaField jf =  DatabaseModels.getInstance().getEntity(JavaField.class).get();

        Optional<JavaClass> jsc =  DatabaseModels.getInstance().getEntity(JavaClass.class, o.idJavaSource);
        Optional<JavaInterface> jsi =  DatabaseModels.getInstance().getEntity(JavaInterface.class, o.idJavaSource);
        Optional<JavaEnum> jse =  DatabaseModels.getInstance().getEntity(JavaEnum.class, o.idJavaSource);

        if(jsc.isPresent()){
            jsc.get().getListOfField();
            jsc.get().addListOfField(jf);
        }
        else if(jsi.isPresent()){
            jsi.get().getListOfField();
            jsi.get().addListOfField(jf);
        }else if(jse.isPresent()){
            jse.get().getListOfField();
            jse.get().addListOfField(jf);
        }
        else{
            Logger.error("Not exhaustive pattern");

        }
//        jsp.getListOfField();
//        jsp.addListOfField(jf);
        jf.setJson(info);
        o.idJavaDoc =jf.getId();


        super.visit(n, o);
    }

    @Override
    public void visit(ImportDeclaration n, MethodVisitorParameter arg) {
        JavaImport javaImport = DatabaseModels.getInstance().getEntity(JavaImport.class).get();
        JavaImportInformation json = new JavaImportInformation();
        json.isAsterisk = n.isAsterisk();
        json.isStatic = n.isStatic();
        json.name = n.getName().toString();

        JavaFile jf = DatabaseModels.getInstance().getEntity(JavaFile.class, arg.idFile).get();
        jf.getListOfJavaImport();
        jf.addlistOfJavaImport(javaImport);
        javaImport.setJson(json);
        super.visit(n, arg);


    }

    @Override
    public void visit(JavadocComment n, MethodVisitorParameter e) {
        if(!n.isLineComment() && e.idJavaDoc!=-1) {

            JavaDocIfoJSON info = new JavaDocIfoJSON();
            info.text = clear(n.toStringWithoutComments());
            JavaDoc jdc = DatabaseModels.getInstance().getEntity(JavaDoc.class).get();
            jdc.setJson(info);
            jdc.setContainsTransverseInformation( e.idJavaDoc);
            e.idJavaDoc = -1;

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

           // JavaInterface method = new JavaInterface();
            JavaInterface method = DatabaseModels.getInstance().getEntity(JavaInterface.class).get();
            JavaFile jf = DatabaseModels.getInstance().getEntity(JavaFile.class,e.idFile).get();
            jf.getListOfJavaInterface();
            jf.addListOfJavaInterface(method);
            method.setJson(thisInfo);
            newId.idJavaSource =method.getId();
            newId.idJavaDoc =  newId.idJavaSource;
            newId.isInterface = true;
        } else {
          //  JavaClass method = new JavaClass();
            JavaClass javaClass = DatabaseModels.getInstance().getEntity(JavaClass.class).get();
            JavaFile jf = DatabaseModels.getInstance().getEntity(JavaFile.class,e.idFile).get();
            jf.getListOfJavaClass();
            jf.addlistOfJavaClass(javaClass);
            javaClass.setJson(thisInfo);
            newId.idJavaSource = javaClass.getId();
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

       // JavaMethod method = new JavaMethod();
        JavaMethod method = DatabaseModels.getInstance().getEntity(JavaMethod.class).get();
        Optional<JavaClass> jsc =  DatabaseModels.getInstance().getEntity(JavaClass.class, arg.idJavaSource);
        Optional<JavaInterface> jsi =  DatabaseModels.getInstance().getEntity(JavaInterface.class, arg.idJavaSource);
        Optional<JavaEnum> jse =  DatabaseModels.getInstance().getEntity(JavaEnum.class, arg.idJavaSource);

        if(jsc.isPresent()){
            jsc.get().getListOfMethod();
            jsc.get().addlistOfMethod(method);
        }
       else if(jsi.isPresent()){
            jsi.get().getListOfMethod();
            jsi.get().addlistOfMethod(method);
        }else if(jse.isPresent()){
            jse.get().getListOfMethod();
            jse.get().addlistOfMethod(method);
        }else{
           Logger.error("Not exhaustive pattern");
        }


        method.setJson(thisInfo);
        arg.idJavaDoc = method.getId();
        arg.javaMethodId =  arg.idJavaDoc;

        super.visit(n, arg);
    }

    @Override
    public void visit(EnumDeclaration n, MethodVisitorParameter arg) {


       // JavaEnum javaEnum =  new JavaEnum();
        JavaEnum javaEnum =  DatabaseModels.getInstance().getEntity(JavaEnum.class).get();
        JavaFile jf = DatabaseModels.getInstance().getEntity(JavaFile.class, arg.idFile).get();

        jf.getListOfJavaEnum();
        jf.addlistOfJavaEnum(javaEnum);
        JavaEnumInformation json = new JavaEnumInformation();
        json.name = n.toString();
        javaEnum.setJson(json);
        arg.idJavaSource =javaEnum.getId();
        arg.idJavaDoc =  arg.idJavaSource;
        arg.isEnum = true;
        super.visit(n, arg);


    }

    @Override
    public void visit(EnumConstantDeclaration n, MethodVisitorParameter arg) {

        JavaEnum javaEnum =  DatabaseModels.getInstance().getEntity(JavaEnum.class).get();
        JavaFile jf = DatabaseModels.getInstance().getEntity(JavaFile.class, arg.idFile).get();
        jf.getListOfJavaEnum();
        jf.addlistOfJavaEnum(javaEnum);
        JavaEnumInformation json = new JavaEnumInformation();
        json.name = n.toString();
        javaEnum.setJson(json);
        arg.idJavaSource =javaEnum.getId();
        arg.idJavaDoc =  arg.idJavaSource;
        super.visit(n, arg);

    }

    @Override
    public void visit(PackageDeclaration n, MethodVisitorParameter arg) {

        JavaPackage javaPackage = DatabaseModels.getInstance().getEntity(JavaPackage.class).get();
        JavaFile jf = DatabaseModels.getInstance().getEntity(JavaFile.class, arg.idFile).get();
        jf.getListOfJavaPackage();
        jf.addlistOfJavaPackage(javaPackage);
        JavaPackageInformation json = new JavaPackageInformation();
        json.name = n.getName().toStringWithoutComments();
        javaPackage.setJson(json);
        super.visit(n, arg);


    }

    @Override
    public void visit(VariableDeclarationExpr n, MethodVisitorParameter arg) {
        super.visit(n, arg);

    }




}

