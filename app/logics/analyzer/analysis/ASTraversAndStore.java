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
import logics.models.modelQuery.Query;
import logics.models.newDatabase.*;
import logics.versionUtils.WebSocketProgress;
import play.Logger;

import java.io.*;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


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
                TextFile analyzedFile =  (TextFile)TextFile.find.where().eq("name", c.getFeatures().getPath()).findUnique();
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
        if(webSocketProgress!=null){
            webSocketProgress.beginTask("Analyse "+currentPath.getPath(),1);
        }
        try {
            analyzedFile = Query.JavaFileByPath(currentPath.getPath()).orElseThrow(() -> new SQLnoResult());
            Long l = Files.size(currentPath.getFilePath());
            if (l != null) {

                analyzedFile.size = l;
                analyzedFile.nline =     Files.lines(currentPath.getFilePath()).count();





            }
        } catch (Exception e) {
            throw new CustomException(e);
        }
        try (InputStream is = Files.newInputStream(currentPath.getFilePath())) {
            CompilationUnit p = JavaParser.parse(is);


            MethodVisitorParameter mvh = new MethodVisitorParameter();
            mvh.idFile = analyzedFile.id;
            mvh.refJavaFile = analyzedFile;

            new MethodVisitor().visit(p, mvh);


            is.close();

        } catch (IOException e) {
            new CustomException(e);
        }  catch (ParseException e) {
            new CustomException(e);
        }finally {

        }
        if(webSocketProgress!=null){
            webSocketProgress.endTask(" Analyse "+currentPath.getPath());
        }

    }
}

/***
 * Only a field class for travers the AST
 */
class MethodVisitorParameter{
    public long idFile;
    public long idJavaSource = -1;
    public long idJavaDoc=0;
    public long javaMethodId=-1;
    public long countJavaMethod= 0;
    public boolean isInterface  = false;
    public boolean isEnum  = false;
    public JavaFile refJavaFile ;

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

        List<JavaMethodCall> result = JavaMethodCall.find.where().eq("methodname",n.getName()).eq("params",n.getArgs().size()).findList();
        JavaMethodCall jmc;
        if(result==null || result.size()==0){
            jmc = new JavaMethodCall();
            jmc.methodname = n.getName();
            jmc.params = n.getArgs().size();

            MethodFile mf = new MethodFile();
            arg.refJavaFile.methodFileList.add(mf);
            mf.javaMethodCall = jmc;
            jmc.methodFileList.add(mf);
            mf.javaFile = arg.refJavaFile;
            Ebean.save(jmc);
            Ebean.save(mf);
            Ebean.update(arg.refJavaFile);

        }else{
            jmc = result.get(0);
            MethodFile mf = new MethodFile();
            arg.refJavaFile.methodFileList.add(mf);
            mf.javaMethodCall = jmc;
            jmc.methodFileList.add(mf);
            mf.javaFile = arg.refJavaFile;
            Ebean.save(jmc);
            Ebean.save(mf);
            Ebean.update(arg.refJavaFile);
        }






        super.visit(n, arg);

    }

    @Override
    public void visit(MethodDeclaration n, MethodVisitorParameter arg) {

        JavaMethod jm = new JavaMethod();
        jm.javaFile = arg.refJavaFile;
        arg.refJavaFile.javaMethodList.add(jm);
        jm.name = n.getName();
        Ebean.save(jm);
        Ebean.update(arg.refJavaFile);
        arg.idJavaDoc = 1;
        super.visit(n, arg);




    }


    @Override
    public void visit(AnnotationDeclaration n, MethodVisitorParameter e) {

        JavaInterface ji = new JavaInterface();
        ji.javaFile = e.refJavaFile;
        e.refJavaFile.javaInterfaceList.add(ji);
        ji.name = n.getName();
        Ebean.save(ji);
        Ebean.update(e.refJavaFile);//
//        MethodInfoJSON thisInfo = new MethodInfoJSON();
//        thisInfo.modifier = "PUBLIC";
//        thisInfo.lineEnd = n.getEndLine();
//        thisInfo.lineStart = n.getBeginLine();
//        thisInfo.signature = clear(n.getName());
//
//        JavaInterface method = DatabaseModels.getInstance().getEntity(JavaInterface.class).get();
//        JavaFile jf = DatabaseModels.getInstance().getEntity(JavaFile.class,e.idFile).get();
//        jf.getListOfJavaInterface();
//        jf.addListOfJavaInterface(method);
//        method.setJson(thisInfo);
//
//        MethodVisitorParameter newId = new MethodVisitorParameter();
//        newId.idFile = e.idFile;
//        newId.idJavaSource =method.getId();
//        newId.idJavaDoc =  newId.idJavaSource;
//        newId.isInterface = true;
        super.visit(n,e);
    }


    @Override
    public void visit(FieldDeclaration n, MethodVisitorParameter o) {

        JavaField jf = new JavaField();
        jf.javaFile =  o.refJavaFile;
        o.refJavaFile.javaFieldList.add(jf);
        jf.name = n.getVariables().get(0).getId().getName();
        Ebean.save(jf);
        Ebean.update(o.refJavaFile);

//        FieldsInfoJSON info = new FieldsInfoJSON();
//        info.type = clear(n.getType().toString());
//        info.modifier = getModifierAsString(n.getModifiers());
//        info.name =   n.getVariables().get(0).getId().getName();
//
//        JavaField jf =  DatabaseModels.getInstance().getEntity(JavaField.class).get();
//
//        Optional<JavaClass> jsc =  DatabaseModels.getInstance().getEntity(JavaClass.class, o.idJavaSource);
//        Optional<JavaInterface> jsi =  DatabaseModels.getInstance().getEntity(JavaInterface.class, o.idJavaSource);
//        Optional<JavaEnum> jse =  DatabaseModels.getInstance().getEntity(JavaEnum.class, o.idJavaSource);
//
//        if(jsc.isPresent()){
//            jsc.get().getListOfField();
//            jsc.get().addListOfField(jf);
//        }
//        else if(jsi.isPresent()){
//            jsi.get().getListOfField();
//            jsi.get().addListOfField(jf);
//        }else if(jse.isPresent()){
//            jse.get().getListOfField();
//            jse.get().addListOfField(jf);
//        }
//        else{
//            Logger.error("Not exhaustive pattern");
//
//        }
////        jsp.getListOfField();
////        jsp.addListOfField(jf);
//        jf.setJson(info);
//        o.idJavaDoc =jf.getId();
//

        super.visit(n, o);
    }

    @Override
    public void visit(ImportDeclaration n, MethodVisitorParameter arg) {
        List<JavaImport> result = JavaImport.find.where().eq("packageName",n.getName().toString()).findList();
        JavaImport jmc;
        if(result==null || result.size()==0){
            jmc = new JavaImport();
            jmc.packageName = n.getName().toString();

            ImportFile mf = new ImportFile();
            mf.javaImport = jmc;
            mf.javaFile = arg.refJavaFile;
            arg.refJavaFile.importFileList.add(mf);
            jmc.importFileList.add(mf);

            Ebean.save(jmc);
            Ebean.save(mf);
            Ebean.update(arg.refJavaFile);

        }else{
            jmc = result.get(0);

            ImportFile mf = new ImportFile();
            mf.javaImport = jmc;
            mf.javaFile = arg.refJavaFile;
            arg.refJavaFile.importFileList.add(mf);
            jmc.importFileList.add(mf);


            Ebean.save(jmc);
            Ebean.save(mf);
            Ebean.update(arg.refJavaFile);
        }





//        JavaImport javaImport = DatabaseModels.getInstance().getEntity(JavaImport.class).get();
//        JavaImportInformation json = new JavaImportInformation();
//        json.isAsterisk = n.isAsterisk();
//        json.isStatic = n.isStatic();
//        json.name = n.getName().toString();
//
//        JavaFile jf = DatabaseModels.getInstance().getEntity(JavaFile.class, arg.idFile).get();
//        jf.getListOfJavaImport();
//        jf.addlistOfJavaImport(javaImport);
//        javaImport.setJson(json);
        super.visit(n, arg);


    }

    @Override
    public void visit(JavadocComment n, MethodVisitorParameter e) {
      //  if(!n.isLineComment() && e.idJavaDoc!=-1) {

            if(e.idJavaDoc==1) {
                JavaDoc jc = new JavaDoc();
                jc.javaFile = e.refJavaFile;
                e.refJavaFile.javaDocList.add(jc);
                jc.name = clear(n.toStringWithoutComments());
                Ebean.save(jc);
                Ebean.update(e.refJavaFile);
                e.idJavaDoc=0;
            }
//            JavaDocIfoJSON info = new JavaDocIfoJSON();
//            info.text = clear(n.toStringWithoutComments());
//            JavaDoc jdc = DatabaseModels.getInstance().getEntity(JavaDoc.class).get();
//            jdc.setJson(info);
//            jdc.setContainsTransverseInformation( e.idJavaDoc);

      //  }
        super.visit(n, e);


    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, MethodVisitorParameter e) {


        if(n.isInterface()){
            JavaInterface ji = new JavaInterface();
            ji.name = n.getName();
            ji.javaFile = e.refJavaFile;

            e.refJavaFile.javaInterfaceList.add(ji);
            Ebean.save(ji);
            Ebean.update(e.refJavaFile);
        }else{
            JavaClass ji = new JavaClass();
            ji.name = n.getName();
            ji.javaFile = e.refJavaFile;
            e.refJavaFile.javaClassList.add(ji);
            Ebean.save(ji);
            Ebean.update(e.refJavaFile);
        }

//        MethodInfoJSON thisInfo = new MethodInfoJSON();
//        thisInfo.modifier = "PUBLIC";
//        thisInfo.lineEnd = n.getEndLine();
//        thisInfo.lineStart = n.getBeginLine();
//        thisInfo.signature = clear(n.getName());
//        MethodVisitorParameter newId = new MethodVisitorParameter();
//        newId.idFile = e.idFile;
//
//        if (n.isInterface()) {
//
////           // JavaInterface method = new JavaInterface();
////            JavaInterface method = DatabaseModels.getInstance().getEntity(JavaInterface.class).get();
////            JavaFile jf = DatabaseModels.getInstance().getEntity(JavaFile.class,e.idFile).get();
////            jf.getListOfJavaInterface();
////            jf.addListOfJavaInterface(method);
////            method.setJson(thisInfo);
////            newId.idJavaSource =method.getId();
////            newId.idJavaDoc =  newId.idJavaSource;
////            newId.isInterface = true;
//        } else {
//          //  JavaClass method = new JavaClass();
//            JavaClass javaClass = DatabaseModels.getInstance().getEntity(JavaClass.class).get();
//            JavaFile jf = DatabaseModels.getInstance().getEntity(JavaFile.class,e.idFile).get();
//            jf.getListOfJavaClass();
//            jf.addlistOfJavaClass(javaClass);
//            javaClass.setJson(thisInfo);
//            newId.idJavaSource = javaClass.getId();
//            newId.idJavaDoc =  newId.idJavaSource;
//        }
//        super.visit(n, newId);
        super.visit(n, e);

    }

    @Override
    public void visit(ConstructorDeclaration n, MethodVisitorParameter arg) {

        JavaMethod jm = new JavaMethod();
        jm.javaFile =  arg.refJavaFile;
        arg.refJavaFile.javaMethodList.add(jm);
        jm.name = n.getName();
        Ebean.save(jm);
        arg.idJavaDoc=1;




        Ebean.update(arg.refJavaFile);

        super.visit(n, arg);

//        MethodInfoJSON thisInfo = new MethodInfoJSON();
//        thisInfo.modifier = clear(getModifierAsString(n.getModifiers()));
//        thisInfo.lineEnd = n.getEndLine();
//        thisInfo.lineStart = n.getBeginLine();
//        thisInfo.signature = clear(n.getName());
//
//       // JavaMethod method = new JavaMethod();
//        JavaMethod method = DatabaseModels.getInstance().getEntity(JavaMethod.class).get();
//        Optional<JavaClass> jsc =  DatabaseModels.getInstance().getEntity(JavaClass.class, arg.idJavaSource);
//        Optional<JavaInterface> jsi =  DatabaseModels.getInstance().getEntity(JavaInterface.class, arg.idJavaSource);
//        Optional<JavaEnum> jse =  DatabaseModels.getInstance().getEntity(JavaEnum.class, arg.idJavaSource);
//
//        if(jsc.isPresent()){
//            jsc.get().getListOfMethod();
//            jsc.get().addlistOfMethod(method);
//        }
//       else if(jsi.isPresent()){
//            jsi.get().getListOfMethod();
//            jsi.get().addlistOfMethod(method);
//        }else if(jse.isPresent()){
//            jse.get().getListOfMethod();
//            jse.get().addlistOfMethod(method);
//        }else{
//           Logger.error("Not exhaustive pattern");
//        }
//
//
//        method.setJson(thisInfo);
//        arg.idJavaDoc = method.getId();
//        arg.javaMethodId =  arg.idJavaDoc;

    }

//    @Override
//    public void visit(EnumDeclaration n, MethodVisitorParameter arg) {
//
//
////       // JavaEnum javaEnum =  new JavaEnum();
////        JavaEnum javaEnum =  DatabaseModels.getInstance().getEntity(JavaEnum.class).get();
////        JavaFile jf = DatabaseModels.getInstance().getEntity(JavaFile.class, arg.idFile).get();
////
////        jf.getListOfJavaEnum();
////        jf.addlistOfJavaEnum(javaEnum);
////        JavaEnumInformation json = new JavaEnumInformation();
////        json.name = n.toString();
////        javaEnum.setJson(json);
////        arg.idJavaSource =javaEnum.getId();
////        arg.idJavaDoc =  arg.idJavaSource;
////        arg.isEnum = true;
////        super.visit(n, arg);
//
//
//    }

//    @Override
//    public void visit(EnumConstantDeclaration n, MethodVisitorParameter arg) {
//
//        JavaEnum javaEnum =  DatabaseModels.getInstance().getEntity(JavaEnum.class).get();
//        JavaFile jf = DatabaseModels.getInstance().getEntity(JavaFile.class, arg.idFile).get();
//        jf.getListOfJavaEnum();
//        jf.addlistOfJavaEnum(javaEnum);
//        JavaEnumInformation json = new JavaEnumInformation();
//        json.name = n.toString();
//        javaEnum.setJson(json);
//        arg.idJavaSource =javaEnum.getId();
//        arg.idJavaDoc =  arg.idJavaSource;
//        super.visit(n, arg);
//
//    }

    @Override
    public void visit(PackageDeclaration n, MethodVisitorParameter arg) {

//        JavaPackage javaPackage = DatabaseModels.getInstance().getEntity(JavaPackage.class).get();
//        JavaFile jf = DatabaseModels.getInstance().getEntity(JavaFile.class, arg.idFile).get();
//        jf.getListOfJavaPackage();
//        jf.addlistOfJavaPackage(javaPackage);
//        JavaPackageInformation json = new JavaPackageInformation();
//        json.name = n.getName().toStringWithoutComments();
//        javaPackage.setJson(json);


        JavaPackage jp = new JavaPackage();
        jp.javaFile = arg.refJavaFile;
        arg.refJavaFile.javaPackageList.add(jp);


        jp.name = n.getName().toString();
        Ebean.save(jp);
        Ebean.update(arg.refJavaFile);
        super.visit(n, arg);


    }

    @Override
    public void visit(VariableDeclarationExpr n, MethodVisitorParameter arg) {
        super.visit(n, arg);

    }




}

