package logics.models.modelQuery;

import exception.CustomException;
import exception.SQLnoResult;
import logics.Definitions;
import logics.databaseCache.DatabaseModels;
import logics.databaseUtilities.Pair;
import logics.models.db.*;
import play.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by bedux on 01/05/16.
 */
public final class Query  {

    /**
     *
     * @param param get All of some class
     * @param <T> The type of return
     * @return  a list of T
     */
    public static <T> List<T> All(Class<T> param) {
        return DatabaseModels.getInstance().getAll(param);
    }

    public static <T> Optional<T> byId(Pair<Long,Class<T> > param) {
        return DatabaseModels.getInstance().getEntity(param.getValue(),param.getKey());
    }
    /**
     *
     * @param javaImport get A list of java import
     * @return  a list of java import discussed
     */
    public static List<JavaImport> AllDiscussedJavaImport(List<JavaImport> javaImport) {
        final List<ImportTable> importDiscussion =All(ImportTable.class);
        return javaImport.stream().filter(x -> {
            long s = importDiscussion
                    .stream()
                    .filter(y ->
                                    x.getJson().name.contentEquals(y.getPackageDiscussion())
                    ).count();
            return s > 0;
        }).collect(Collectors.toList());
    }

    public static List<StackOFDiscussion> AllDiscussionHavingMethodName(List<String> param) {
        return  All(MethodDiscussion.class)
                .stream()
                .filter(x->param.stream().anyMatch(y -> y.contentEquals(x.getMethodConcrete().getMethodName())))
                .map(x->x.getDiscussionConcrete())
                .collect(Collectors.toList());
    }

    public static List<ImportTable> AllImportTableFormJavaImport(List<JavaImport> javaImport) {
       return All(ImportTable.class)
                .stream()
                .filter(y -> {
                    long s = javaImport
                            .stream()
                            .filter(x -> x.getJson().name.contentEquals(y.getPackageDiscussion()))
                            .count();
                    return s > 0;
                })
                .collect(Collectors.toList());
    }

    public static List<ImportTable> AllImportFromDiscussion(Void param) {
        return All(ImportTable.class);
    }

    public static List<JavaClass> AllJavaClassByJavaFile(Long id) {
        return All(JavaClass.class)
                .stream()
                .filter(x -> x.getJavaFileConcrete().getId() == id)
                .collect(Collectors.toList());
    }

    public static List<JavaField> AllJavaFieldsFormPath(String path) {
        return All(JavaField.class)
                .stream()
                .filter(x -> x.getJavaClassConcrete().getJavaFileConcrete().getPath().equals(path))
                .collect(Collectors.toList());

    }

    public static List<JavaImport> AllJavaImportFromJavaFile(Long param) {
        return All(JavaImport.class).stream().filter(x->x.getJavaFileConcrete().getId() == param).collect(Collectors.toList());
    }

    public static List<JavaInterface> AllJavaInterfaceByJavaFile(Long id) {
        return All(JavaInterface.class).stream().filter(x->x.getJavaFileConcrete().getId() ==id).collect(Collectors.toList());
    }

    public  static List<String> AllJavaMethodCallFormPath(String path) {
        List<String> res = All(JavaMethod.class)
                .stream()
                .filter(x -> x.getJavaClassConcrete().getJavaFileConcrete().getPath().equals(path))
                .filter(x -> x.getJson() != null)
                .flatMap(x -> x.getJson().variableDeclaration.stream())
                .collect(Collectors.toList());
        res.addAll(
                All(JavaClass.class)
                        .stream()
                        .filter(x -> x.getJavaFileConcrete().getPath().equals(path))
                        .filter(x->x.getJson() != null)
                        .flatMap(x -> x.getJson().variableDeclaration.stream())
                        .collect(Collectors.toList()));

        res.addAll(
                All(JavaInterface.class)
                        .stream()
                        .filter(x -> x.getJavaFileConcrete().getPath().equals(path))
                        .filter(x->x.getJson() != null)
                        .flatMap(x -> x.getJson().variableDeclaration.stream())
                        .collect(Collectors.toList()));
        return res;
    }

    public static List<JavaMethod> AllJavaMethodFormPath(String path) {
        return All(JavaMethod.class).stream().filter(x -> x.getJavaClassConcrete().getJavaFileConcrete().getPath().equals(path)).collect(Collectors.toList());

    }

    public static List<String> AllJavaMethodOfRepositoryVersion(Long id) {
        return All(JavaMethod.class).stream().filter(x -> x.getJavaClassConcrete().getJavaFileConcrete().getRepositoryVersionConcrete().getId() == id).map(x -> x.getJson().signature)
                .collect(Collectors.toList());

    }

    public static List<MethodTable> AllMethodDiscussed(List<String> param) {
        return  All(MethodDiscussion.class)
                .stream()
                .filter(x->param.stream().filter(y -> y.equals(x.getMethodConcrete().getMethodName())).count()>0)
                .map(x->x.getMethodConcrete())
                .distinct()
                .collect(Collectors.toList());
    }

    public static List<JavaImport> AllNonLocalImport(Pair<Long, Long> param) {
        final List<JavaImport> javaImports =AllJavaImportFromJavaFile(param.getKey());
        final List<JavaPackage> packages = AllPackagesFromRepositoryVersion(param.getValue());
        return javaImports.stream().
                filter(x ->
                        {
                                    final Boolean bb = !packages.parallelStream().anyMatch(y -> {
                                    final Boolean result = x.getJson().name.contains(y.getJson().name);
                                    return result;
                            });
                            return bb;
                        }
                ).collect(Collectors.toList());
    }

    public static List<JavaPackage> AllPackagesFromRepositoryVersion(Long param) {
        final List<JavaPackage> importDiscussion = All(JavaPackage.class)
                .stream()
                .filter(x -> x.getJavaFileConcrete().getRepositoryVersionConcrete().getId() == param)
                .collect(Collectors.toList());
        return importDiscussion;
    }

    public static List<RepositoryRender> AllRepositoryVersionRender(Void param) {
        return All(RepositoryRender.class);

    }

    public static Long CountFieldByPath(String param) {
        return (long)AllJavaFieldsFormPath(param).size();
    }

    public static Long CountJavaDocMethodsByPath(String param) {
        List<JavaFile> jfs = All(JavaFile.class).stream().filter(x->x.getPath().equals(param)).collect(Collectors.toList());
        final List<Long> l = jfs.stream().flatMap(x ->
                {
                    final List<JavaClass> javaClassList = x.getListOfJavaClass();
                    return javaClassList.stream().flatMap(y ->
                    {
                        final List<JavaMethod> javaMethods =   y.getListOfMethod();
                        return javaMethods.stream().map(z -> (Long) z.getId());
                    });
                }
        ).collect(Collectors.toList());

        return All(JavaDoc.class).stream()
                .filter(x->l.stream().anyMatch(y -> x.getContainsTransverseInformation()==y)).count();
    }

    public static Long CountMethodByPath(String param) {
        return (long)AllJavaMethodFormPath(param).size();
    }

    public static List<String> NonProjectMethodCall(String path){
        final JavaFile jf =JavaFileByPath(path).orElseThrow(() -> new SQLnoResult());
        final List<String> allJavaMethods  =  AllJavaMethodOfRepositoryVersion(jf.getRepositoryVersionConcrete().getId()).stream().distinct().collect(Collectors.toList());
        final List<String> pathMethodCall = AllJavaMethodCallFormPath(path)
                .stream()
                .distinct()
                .collect(Collectors.toList());
        return  pathMethodCall.stream().filter(x -> allJavaMethods.stream().filter(z -> z.equals(x)).count() == 0).collect(Collectors.toList());

    }

    public static List<MethodTable> AllDiscussedMethod(String path){
       return AllMethodDiscussed(NonProjectMethodCall(path));
    }

    public static List<ImportTable> AllDiscussedImport(String path){
        final JavaFile jf =JavaFileByPath(path).orElseThrow(() -> new SQLnoResult());

        final List<JavaImport> nonLocalImport =   AllNonLocalImport(new Pair(jf.getId(), jf.getRepositoryVersionConcrete().getId()));
        return AllImportTableFormJavaImport(AllDiscussedJavaImport(nonLocalImport));
    }

    public static Long DiscussedImportMethodCounter(String path) {

        final long numberOfMethodDiscussed = AllDiscussedMethod(path).stream().map(x -> x.getMethodName()).distinct().count();
        final long numberOfImportDiscussed = AllDiscussedImport(path).stream().map(x->x.getPackageDiscussion()).distinct().count();
        return numberOfMethodDiscussed + numberOfImportDiscussed;

    }

    public static Long DiscussedImportMethodCounterOverTotal(String path) {
        final JavaFile jf =JavaFileByPath(path).orElseThrow(() -> new SQLnoResult());

        final float numberOfMethod = NonProjectMethodCall(path).size();
        final float numberOfImport = AllNonLocalImport(new Pair(jf.getId(), jf.getRepositoryVersionConcrete().getId())).size();


        final float numberOfMethodDiscussed = AllDiscussedMethod(path).stream().map(x -> x.getMethodName()).distinct().count();
        final float numberOfImportDiscussed = AllDiscussedImport(path).stream().map(x->x.getPackageDiscussion()).distinct().count();

        float t1 = 100;
        if(numberOfMethod!=0){
            t1 = (numberOfMethodDiscussed/numberOfMethod)*100;
            if(t1>100){
                throw new CustomException("Percentage > then 100 over Methods");
            }

        }
        float t2 = 100;

        if(numberOfImport!=0){
            t2 =(numberOfImportDiscussed/numberOfImport)*100;
            if(t2>100){
                throw new CustomException("Percentage > then 100 over Field");
            }

        }

        return (long)( (t1+t2)/2.0);

    }

    public static List<File>  FileByRepositoryVersion(Long param) {
        return  All(File.class).stream().filter(x->x.getRepositoryVersionConcrete().getId() == param).collect(Collectors.toList());
    }

    public static Optional<JavaFile> JavaFileByPath(String path) {
        return All(JavaFile.class).stream().filter(x->x.getPath().equals(path)).findFirst();
    }

    public static long NumberOfFile(Long param) {
        return All(File.class).stream().filter(x->x.getRepositoryVersionConcrete().getId()==param).count();
    }

    public static String ProjectName(Long param) {
        return byId(new Pair<>(param,RepositoryVersion.class))
                .get()
                .getRepository()
                .getUrl();
    }

    public static  Optional<TextFile> TextFileByPath(String param) {
        return All(TextFile.class).stream().filter(x->x.getPath().equals(param)).findFirst();
    }

    public static Long TotalNumberOfCodeLines(Long id) {
        return (long) (All(JavaFile.class).stream().filter(x->x.getRepositoryVersionConcrete().getId()==id).map(x->x.getJson().noLine)
                .reduce((x,y)->x+y).get());
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

        List<JavaFile> jfs = All(JavaFile.class)
                            .stream()
                            .filter(x -> x.getPath().equals(path))
                            .collect(Collectors.toList());

        final List<Long> l = jfs.stream().flatMap(x ->
                {
                    final List<JavaClass> javaClassList = x.getListOfJavaClass();
                    return javaClassList.stream().flatMap(y ->
                    {
                        final List<JavaMethod> javaMethods =   y.getListOfMethod();
                        return javaMethods.stream().map(z -> (Long) z.getId());
                    });
                }
        ).collect(Collectors.toList());

        return All(JavaDoc.class)
                .stream()
                .filter(x->l.stream().anyMatch(y -> x.getContainsTransverseInformation()==y))
                .count();


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
        return jf.getListOfJavaClass().size();
    }

    public static long CountJavaInterface(String path){
        JavaFile jf = JavaFileByPath(path).orElseThrow(() -> new CustomException(""));
        return jf.getListOfJavaInterface().size();
    }

    public static long CountJavaEnum(String path){
        JavaFile jf = JavaFileByPath(path).orElseThrow(() -> new CustomException(""));
        return jf.getListOfJavaEnum().size();
    }

    public static long CountJavaImport(String path){
        JavaFile jf = JavaFileByPath(path).orElseThrow(() -> new CustomException(""));
        return jf.getListOfJavaImport().size();
    }
    public static long CountMethodCall(String path){
        return AllJavaMethodCallFormPath(path).size();

    }

}
