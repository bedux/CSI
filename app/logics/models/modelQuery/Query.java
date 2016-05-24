package logics.models.modelQuery;

import exception.CustomException;
import exception.SQLnoResult;
import logics.Definitions;
import logics.analyzer.UsedData;
import logics.databaseUtilities.Pair;
import logics.models.newDatabase.*;
import play.db.ebean.Model;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;



public  class Query  {


    private    String computeHash(String param,String call){
        return param+"#"+call;
    }

    /**
     *
     * @param param get All of some class
     * @param <T> The type of return
     * @return  a list of T
     */
    public    <T extends Model> List<T> All(Model.Finder param) {
        return param.all();

    }



    public   <T> Optional<T> byId(Pair<Long,Model.Finder> param) {
      //  return DatabaseModels.getInstance().getEntity(param.getValue(),param.getKey());
        Object r = param.getValue().byId(param.getKey());
        if(r==null) return Optional.empty();
        return Optional.of((T)r);
    }

//    public static <T> Optional<T> byId(Pair<Long,Class<T> > param) {
//        return DatabaseModels.getInstance().getEntity(param.getValue(),param.getKey());
//    }
    /**
     *
     * @param javaImport get A list of java import
     * @return  a list of java import discussed
     */
    public   List<JavaImport> AllDiscussedJavaImport(List<JavaImport> javaImport) {
        return javaImport.stream().filter(x->x.importDiscussionList.size()>0).collect(Collectors.toList());
    }

    public  List<JavaClass> AllJavaClassByJavaFile(Long id) {
//        return All(JavaClass.class)
//                .stream()
//                .filter(x -> x.getJavaFileConcrete().getId() == id)
//                .collect(Collectors.toList());
        Optional<JavaFile> res  = byId(new Pair<>(id,JavaFile.find));
        if(res.isPresent()){
            return res.get().javaClassList;
        }
        return new ArrayList<>();
    }

    public <T> List<T> getBySome(Model.Finder f, String column, String target){
        return (List<T>)(f.where().eq(column,target).findList());
    }

    public   List<JavaField> AllJavaFieldsFormPath(String path) {
         JavaFile jf = (JavaFile) getBySome(JavaFile.find,"name",path).get(0);
        return jf.javaFieldList;
    }

    public   List<JavaImport> AllJavaImportFromJavaFile(Long param) {
        JavaFile jf = (JavaFile) byId(new Pair<>(param,JavaFile.find)).get();
        return jf.importFileList.stream().map(x->x.javaImport).collect(Collectors.toList());
    }

    public   List<JavaInterface> AllJavaInterfaceByJavaFile(Long id) {
        JavaFile jf = (JavaFile) byId(new Pair<>(id,JavaFile.find)).get();
        return jf.javaInterfaceList;
    }

    public List<JavaMethodCall> AllJavaMethodCallFormPath(String path) {
        JavaFile jf = (JavaFile) getBySome(JavaFile.find,"name",path).get(0);
        return  jf.methodFileList.stream().map(x->x.javaMethodCall).collect(Collectors.toList());
    }

    public   List<JavaMethod> AllJavaMethodFormPath(String path) {
        JavaFile jf = (JavaFile) getBySome(JavaFile.find,"name",path).get(0);
        return jf.javaMethodList;
    }

    public  List<JavaMethod> AllJavaMethodOfRepositoryVersion(Long id) {

        RepositoryVersion r =(RepositoryVersion)byId(new Pair<>(id,RepositoryVersion.find)).get();
        return r.javaFileList.stream().flatMap(x->{
                if(x.javaMethodList!=null)
                    return x.javaMethodList.stream();
                else{
                    System.out.println(x.id);
                    return new ArrayList<JavaMethod>().stream();}
        }).collect(Collectors.toList());

    }

//    public static List<JavaMethodCall> AllMethodDiscussed(List<String> param) {
//        return  All(MethodDiscussion.class)
//                .stream()
//                .filter(x->param.stream().filter(y -> y.equals(x.getMethodName())).count()>0)
//                .map(x->x.getMethodConcrete())
//                .distinct()
//                .collect(Collectors.toList());
//    }

    public  List<JavaImport> AllNonLocalImport(Pair<Long, Long> param) {
        final List<JavaImport> javaImports =AllJavaImportFromJavaFile(param.getKey());
        final List<JavaPackage> packages = AllPackagesFromRepositoryVersion(param.getValue());
        return javaImports.stream().
                filter(x ->
                        {
                                    final Boolean bb = !packages.stream().anyMatch(y -> {
                                    final Boolean result = x.packageName.contains(y.name);
                                    return result;
                            });
                            return bb;
                        }
                ).collect(Collectors.toList());
    }

    public  List<JavaPackage> AllPackagesFromRepositoryVersion(Long param) {
        RepositoryVersion jf = (RepositoryVersion) byId(new Pair<>(param,RepositoryVersion.find)).get();
        return jf.javaFileList.stream().filter(x->x.javaPackageList!=null).flatMap(x->x.javaPackageList.stream()).collect(Collectors.toList());
    }

    public   List<RepositoryRender> AllRepositoryVersionRender(Void param) {
        return All(RepositoryRender.find);

    }

    @Deprecated
    public  Long CountFieldByPath(String param) {
        Function<String,Long> func = (x)->(long)AllJavaFieldsFormPath(param).size();


        return (long)AllJavaFieldsFormPath(param).size();
    }

    public  Function<String,Long> CountFieldByPathWrap = UsedData.<String>wrapCache(
            (x)->(long)AllJavaFieldsFormPath(x).size(),
            (x)->x.countFieldByPath,
            (x,y)->x.countFieldByPath=y);

    public  Function<String,Long> CountMethodByPathWrap = UsedData.<String>wrapCache(
            (x)->(long)AllJavaMethodFormPath(x).size(),
            (x)->x.countMethodByPath,
            (x,y)->x.countMethodByPath=y);


    public Long CountJavaDocMethodsByPath(String param) {
        JavaFile jf = (JavaFile) getBySome(JavaFile.find,"name",param).get(0);
        return  (long)jf.javaDocList.size();
//        List<JavaFile> jfs = All(JavaFile.class).stream().filter(x->x.getPath().equals(param)).collect(Collectors.toList());
//        final List<Long> l = jfs.stream().flatMap(x ->
//                {
//                    final List<JavaClass> javaClassList = x.getListOfJavaClass();
//                    return javaClassList.stream().flatMap(y ->
//                    {
//                        final List<JavaMethod> javaMethods =   y.getListOfMethod();
//                        return javaMethods.stream().map(z -> (Long) z.getId());
//                    });
//                }
//        ).collect(Collectors.toList());

//        return All(JavaDoc.class).stream()
//                .filter(x->l.stream().anyMatch(y -> x.getContainsTransverseInformation()==y)).count();
    }

    @Deprecated
    public Long CountMethodByPath(String param) {
        return (long)AllJavaMethodFormPath(param).size();
    }

    public  List<JavaMethodCall> NonProjectMethodCall(String path){
        final JavaFile jf =JavaFileByPath(path).orElseThrow(() -> new SQLnoResult());
        final List<JavaMethod> allJavaMethods  =  AllJavaMethodOfRepositoryVersion(jf.repositoryVersion.id).stream().distinct().collect(Collectors.toList());
        final List<JavaMethodCall> pathMethodCall = AllJavaMethodCallFormPath(path)
                .stream()
                .distinct()
                .collect(Collectors.toList());
        return  pathMethodCall.stream().filter(x -> allJavaMethods.stream().filter(z -> z.equals(x)).count() == 0).collect(Collectors.toList());

    }

    public List<JavaMethodCall> AllDiscussedMethod(String path){

       final List<JavaMethodCall> jmc =  NonProjectMethodCall(path).stream().filter(x->x.methodDiscussionList!=null).filter(x->x.methodDiscussionList.size()>0).collect(Collectors.toList());
        return jmc;
    }

    public List<JavaImport> AllDiscussedImport(String path){
        final JavaFile jf =JavaFileByPath(path).orElseThrow(() -> new SQLnoResult());

        final List<JavaImport> nonLocalImport =   AllNonLocalImport(new Pair(jf.id, jf.repositoryVersion.id));

        return AllDiscussedJavaImport(nonLocalImport);
    }

    public  Long DiscussedImportMethodCounter(String path) {

        final JavaFile jf =JavaFileByPath(path).orElseThrow(() -> new SQLnoResult());

        final List<JavaMethodCall> numberOfMethod = NonProjectMethodCall(path);
        final List<JavaImport> numberOfImport = AllNonLocalImport(new Pair(jf.id, jf.repositoryVersion.id));


        final long numberOfMethodDiscussed = numberOfMethod.stream().filter(x->x.methodDiscussionList.size()>0).count();
        final long numberOfImportDiscussed = numberOfImport.stream().filter(x->x.importDiscussionList.size()>0).count();

        return numberOfMethodDiscussed + numberOfImportDiscussed;

    }

    public  Function<String,Long> DiscussedImportMethodCounterWrap = UsedData.<String>wrapCache(
            (x)->DiscussedImportMethodCounter(x),
            (x)->x.discussedImportMethodCounter,
            (x,y)->x.discussedImportMethodCounter=y);

    public  Long DiscussedImportMethodCounterOverTotal(String path) {
        final JavaFile jf =JavaFileByPath(path).orElseThrow(() -> new SQLnoResult());

        final List<JavaMethodCall> numberOfMethod = NonProjectMethodCall(path);
        final List<JavaImport> numberOfImport = AllNonLocalImport(new Pair(jf.id, jf.repositoryVersion.id));


        final float numberOfMethodDiscussed = numberOfMethod.stream().filter(x->x.methodDiscussionList.size()>0).count();
        final float numberOfImportDiscussed = numberOfImport.stream().filter(x->x.importDiscussionList.size()>0).count();

        float t1 = 0;
        if(numberOfMethod.size()!=0){
            t1 = (numberOfMethodDiscussed/numberOfMethod.size())*100;
            if(t1>100){
                throw new CustomException("Percentage > then 100 over Methods");
            }

        }
        float t2 = 0;

        if(numberOfImport.size()!=0){
            t2 =(numberOfImportDiscussed/numberOfImport.size())*100;
            if(t2>100){
                throw new CustomException("Percentage > then 100 over Field");
            }

        }

        return (long)( (t1+t2)/2.0);

    }

    public  Function<String,Long> DiscussedImportMethodCounterOverTotalWrap = UsedData.<String>wrapCache(
            (x)->DiscussedImportMethodCounterOverTotal(x),
            (x)->x.discussedImportMethodCounterOverTotal,
            (x,y)->x.discussedImportMethodCounterOverTotal=y);

//    public static List<File>  FileByRepositoryVersion(Long param) {
//        return  All(File.class).stream().filter(x->x.getRepositoryVersionConcrete().getId() == param).collect(Collectors.toList());
//    }

    public  Optional<JavaFile> JavaFileByPath(String path) {
        return Optional.of((JavaFile)getBySome(JavaFile.find,"name",path).get(0)); //TODO
    }

    public  long NumberOfFile(Long param) {
        RepositoryVersion repo =(RepositoryVersion)RepositoryVersion.find.byId(param);
        return repo.binaryFileList.size()+repo.javaFileList.size()+repo.textFileList.size();
    }

    public  String ProjectName(Long param) {
        return ((RepositoryVersion)byId(new Pair<>(param,RepositoryVersion.find)).get()).repository.url;

    }

//    public static  Optional<TextFile> TextFileByPath(String param) {
//        return All(TextFile.class).stream().filter(x->x.getPath().equals(param)).findFirst();
//    }

    public  Long TotalNumberOfCodeLines(Long id) {
        return this.<RepositoryVersion>byId(new Pair<>(id,RepositoryVersion.find)).get().javaFileList.stream().map(x->x.nline).reduce((x,y)->x+y).get();

    }

    public  Long TotalSize(Long param) {
        try {
            return Files.walk(new java.io.File(Definitions.repositoryPath + param).toPath())
                    .filter(path -> !Files.isDirectory(path)).mapToLong(x -> x.toFile().length()).sum();
        } catch (IOException e) {
            throw new CustomException(e);
        }
    }

    public  long JavaDocForMethodCount(String path){
        return JavaFileByPath(path).get().javaDocList.size();
    }

    public  Function<String,Long> JavaDocForMethodCountWrap = UsedData.<String>wrapCache(
            (x)->JavaDocForMethodCount(x),
            (x)->x.javaDocForMethodCount,
            (x,y)->x.javaDocForMethodCount=y);

    public  long JavaDocOverTotalMethods(String path){

        final float totJavaDoc = JavaDocForMethodCount(path);
        final float totMethod = AllJavaMethodFormPath(path).size();

        if(totMethod!=0){
            return (long) ((totJavaDoc/totMethod)*100);
        }
        return 0L;

    }

    public  Function<String,Long> JavaDocOverTotalMethodsWrap = UsedData.<String>wrapCache(
            (x)->JavaDocOverTotalMethods(x),
            (x)->x.javaDocOverTotalMethods,
            (x,y)->x.javaDocOverTotalMethods=y);


    public  long CountJavaClass(String path){
        JavaFile jf = JavaFileByPath(path).orElseThrow(() -> new CustomException(""));
        return jf.javaClassList.size();
    }

    public  long CountJavaInterface(String path){
        JavaFile jf = JavaFileByPath(path).orElseThrow(() -> new CustomException(""));
        return jf.javaInterfaceList.size();
    }

//    public static long CountJavaEnum(String path){
//        JavaFile jf = JavaFileByPath(path).orElseThrow(() -> new CustomException(""));
//        return jf.getListOfJavaEnum().size();
//    }

    public  long CountJavaImport(String path){
        JavaFile jf = JavaFileByPath(path).orElseThrow(() -> new CustomException(""));
        return jf.importFileList.size();
    }
    public  long CountMethodCall(String path){
        return AllJavaMethodCallFormPath(path).size();

    }

    private  boolean listCOntains(List<String> list,String s){
        for(String s1:list){
            if(s1.contentEquals(s)){
                return true;
            }
        }
        return false;
    }




}
