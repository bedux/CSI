package logics.models.modelQuery;

import exception.CustomException;
import exception.SQLnoResult;
import logics.Definitions;
import logics.databaseUtilities.Pair;
import logics.models.newDatabase.*;
import play.db.ebean.Model;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



public final class Query  {

    private static HashMap<String,Object> cache = new HashMap<>();

    private static String computeHash(String param,String call){
        return param+"#"+call;
    }

    /**
     *
     * @param param get All of some class
     * @param <T> The type of return
     * @return  a list of T
     */
    public static <T extends Model> List<T> All(Model.Finder param) {
        return param.all();

    }



    public static <T> Optional<T> byId(Pair<Long,Model.Finder> param) {
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
    public static List<JavaImport> AllDiscussedJavaImport(List<JavaImport> javaImport) {
        return javaImport.stream().filter(x->x.importDiscussionList.size()>0).collect(Collectors.toList());
    }

//    public static List<Discussion> AllDiscussionHavingMethodName(List<String> param) {
//        return  All(MethodDiscussion.class)
//                .stream()
//                .filter(x->param.stream().anyMatch(y -> y.contentEquals(x.getMethodConcrete().getMethodName())))
//                .map(x->x.getDiscussionConcrete())
//                .collect(Collectors.toList());
//    }

//    public static List<ImportTable> AllImportTableFormJavaImport(List<JavaImport> javaImport) {
//       return All(ImportTable.class)
//                .stream()
//                .filter(y -> {
//                    long s = javaImport
//                            .stream()
//                            .filter(x -> x.getJson().name.contentEquals(y.getPackageDiscussion()))
//                            .count();
//                    return s > 0;
//                })
//                .collect(Collectors.toList());
//    }

//    public static List<ImportTable> AllImportFromDiscussion(Void param) {
//        return All(ImportTable.class);
//    }

    public static List<JavaClass> AllJavaClassByJavaFile(Long id) {
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

    public static <T> List<T> getBySome(Model.Finder f, String column, String target){
        return (List<T>)(f.where().eq(column,target).findList());
    }

    public static List<JavaField> AllJavaFieldsFormPath(String path) {
         JavaFile jf = (JavaFile) getBySome(JavaFile.find,"name",path).get(0);
        return jf.javaFieldList;
    }

    public static List<JavaImport> AllJavaImportFromJavaFile(Long param) {
        JavaFile jf = (JavaFile) byId(new Pair<>(param,JavaFile.find)).get();
        return jf.importFileList.stream().map(x->x.javaImport).collect(Collectors.toList());
    }

    public static List<JavaInterface> AllJavaInterfaceByJavaFile(Long id) {
        JavaFile jf = (JavaFile) byId(new Pair<>(id,JavaFile.find)).get();
        return jf.javaInterfaceList;
    }

    public  static List<JavaMethodCall> AllJavaMethodCallFormPath(String path) {
        JavaFile jf = (JavaFile) getBySome(JavaFile.find,"name",path).get(0);
        return  jf.methodFileList.stream().map(x->x.javaMethodCall).collect(Collectors.toList());
    }

    public static List<JavaMethod> AllJavaMethodFormPath(String path) {
        JavaFile jf = (JavaFile) getBySome(JavaFile.find,"name",path).get(0);
        return jf.javaMethodList;
    }

    public static List<JavaMethod> AllJavaMethodOfRepositoryVersion(Long id) {

        RepositoryVersion r =(RepositoryVersion)byId(new Pair<>(id,RepositoryVersion.find)).get();
        return r.javaFileList.stream().flatMap(x->x.javaMethodList.stream()).collect(Collectors.toList());

    }

//    public static List<JavaMethodCall> AllMethodDiscussed(List<String> param) {
//        return  All(MethodDiscussion.class)
//                .stream()
//                .filter(x->param.stream().filter(y -> y.equals(x.getMethodName())).count()>0)
//                .map(x->x.getMethodConcrete())
//                .distinct()
//                .collect(Collectors.toList());
//    }

    public static List<JavaImport> AllNonLocalImport(Pair<Long, Long> param) {
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

    public static List<JavaPackage> AllPackagesFromRepositoryVersion(Long param) {
        RepositoryVersion jf = (RepositoryVersion) byId(new Pair<>(param,RepositoryVersion.find)).get();
        return jf.javaFileList.stream().flatMap(x->x.javaPackageList.stream()).collect(Collectors.toList());
    }

    public static List<RepositoryRender> AllRepositoryVersionRender(Void param) {
        return All(RepositoryRender.find);

    }

    public static Long CountFieldByPath(String param) {
        return (long)AllJavaFieldsFormPath(param).size();
    }

    public static Long CountJavaDocMethodsByPath(String param) {
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

    public static Long CountMethodByPath(String param) {
        return (long)AllJavaMethodFormPath(param).size();
    }

    public static List<JavaMethodCall> NonProjectMethodCall(String path){
        final JavaFile jf =JavaFileByPath(path).orElseThrow(() -> new SQLnoResult());
        final List<JavaMethod> allJavaMethods  =  AllJavaMethodOfRepositoryVersion(jf.repositoryVersion.id).stream().distinct().collect(Collectors.toList());
        final List<JavaMethodCall> pathMethodCall = AllJavaMethodCallFormPath(path)
                .stream()
                .distinct()
                .collect(Collectors.toList());
        return  pathMethodCall.stream().filter(x -> allJavaMethods.stream().filter(z -> z.equals(x)).count() == 0).collect(Collectors.toList());

    }

    public static List<JavaMethodCall> AllDiscussedMethod(String path){

       return NonProjectMethodCall(path).stream().filter(x->x.methodDiscussionList.size()>0).collect(Collectors.toList());
    }

    public static List<JavaImport> AllDiscussedImport(String path){
        final JavaFile jf =JavaFileByPath(path).orElseThrow(() -> new SQLnoResult());

        final List<JavaImport> nonLocalImport =   AllNonLocalImport(new Pair(jf.id, jf.repositoryVersion.id));
        return AllDiscussedJavaImport(nonLocalImport);
    }

    public static Long DiscussedImportMethodCounter(String path) {

        final JavaFile jf =JavaFileByPath(path).orElseThrow(() -> new SQLnoResult());

        final List<JavaMethodCall> numberOfMethod = NonProjectMethodCall(path);
        final List<JavaImport> numberOfImport = AllNonLocalImport(new Pair(jf.id, jf.repositoryVersion.id));


        final long numberOfMethodDiscussed = numberOfMethod.stream().filter(x->x.methodDiscussionList.size()>0).count();
        final long numberOfImportDiscussed = numberOfImport.stream().filter(x->x.importDiscussionList.size()>0).count();

        return numberOfMethodDiscussed + numberOfImportDiscussed;

    }

    public static Long DiscussedImportMethodCounterOverTotal(String path) {
        final JavaFile jf =JavaFileByPath(path).orElseThrow(() -> new SQLnoResult());

        final List<JavaMethodCall> numberOfMethod = NonProjectMethodCall(path);
        final List<JavaImport> numberOfImport = AllNonLocalImport(new Pair(jf.id, jf.repositoryVersion.id));


        final float numberOfMethodDiscussed = numberOfMethod.stream().filter(x->x.methodDiscussionList.size()>0).count();
        final float numberOfImportDiscussed = numberOfImport.stream().filter(x->x.importDiscussionList.size()>0).count();

        float t1 = 100;
        if(numberOfMethod.size()!=0){
            t1 = (numberOfMethodDiscussed/numberOfMethod.size())*100;
            if(t1>100){
                throw new CustomException("Percentage > then 100 over Methods");
            }

        }
        float t2 = 100;

        if(numberOfImport.size()!=0){
            t2 =(numberOfImportDiscussed/numberOfImport.size())*100;
            if(t2>100){
                throw new CustomException("Percentage > then 100 over Field");
            }

        }

        return (long)( (t1+t2)/2.0);

    }

//    public static List<File>  FileByRepositoryVersion(Long param) {
//        return  All(File.class).stream().filter(x->x.getRepositoryVersionConcrete().getId() == param).collect(Collectors.toList());
//    }

    public static Optional<JavaFile> JavaFileByPath(String path) {
        return Optional.of((JavaFile)getBySome(JavaFile.find,"name",path).get(0)); //TODO
    }

    public static long NumberOfFile(Long param) {
        return JavaFile.find.all().size()+TextFile.find.all().size()+BinaryFile.find.all().size();
    }

    public static String ProjectName(Long param) {
        return ((RepositoryVersion)byId(new Pair<>(param,RepositoryVersion.find)).get()).repository.url;

    }

//    public static  Optional<TextFile> TextFileByPath(String param) {
//        return All(TextFile.class).stream().filter(x->x.getPath().equals(param)).findFirst();
//    }

    public static Long TotalNumberOfCodeLines(Long id) {
        return Query.<RepositoryVersion>byId(new Pair<>(id,RepositoryVersion.find)).get().javaFileList.stream().map(x->x.nline).reduce((x,y)->x+y).get();

    }

    public static Long TotalSize(Long param) {
        try {
            return Files.walk(new java.io.File(Definitions.repositoryPath + param).toPath())
                    .filter(path -> !Files.isDirectory(path)).mapToLong(x -> x.toFile().length()).sum();
        } catch (IOException e) {
            throw new CustomException(e);
        }
    }

    public static long JavaDocForMethodCount(String path){
        return JavaFileByPath(path).get().javaDocList.size();

//        List<JavaFile> jfs = All(JavaFile.class)
//                            .stream()
//                            .filter(x -> x.getPath().equals(path))
//                            .collect(Collectors.toList());
//
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
//
//        return All(JavaDoc.class)
//                .stream()
//                .filter(x->l.stream().anyMatch(y -> x.getContainsTransverseInformation()==y))
//                .count();


    }

    public static long JavaDocOverTotalMethods(String path){

        final float totJavaDoc = JavaDocForMethodCount(path);
        final float totMethod = AllJavaMethodFormPath(path).size();

        if(totMethod!=0){
            return (long) ((totJavaDoc/totMethod)*100);
        }
        return 0L;

    }


    public static long CountJavaClass(String path){
        JavaFile jf = JavaFileByPath(path).orElseThrow(() -> new CustomException(""));
        return jf.javaClassList.size();
    }

    public static long CountJavaInterface(String path){
        JavaFile jf = JavaFileByPath(path).orElseThrow(() -> new CustomException(""));
        return jf.javaInterfaceList.size();
    }

//    public static long CountJavaEnum(String path){
//        JavaFile jf = JavaFileByPath(path).orElseThrow(() -> new CustomException(""));
//        return jf.getListOfJavaEnum().size();
//    }

    public static long CountJavaImport(String path){
        JavaFile jf = JavaFileByPath(path).orElseThrow(() -> new CustomException(""));
        return jf.importFileList.size();
    }
    public static long CountMethodCall(String path){
        return AllJavaMethodCallFormPath(path).size();

    }

    private static boolean listCOntains(List<String> list,String s){
        for(String s1:list){
            if(s1.contentEquals(s)){
                return true;
            }
        }
        return false;
    }




}
