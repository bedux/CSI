package logics.models.query;

import logics.DatabaseManager;
import logics.Definitions;
import logics.databaseUtilities.IDatabaseClass;
import logics.models.db.*;
import play.libs.Json;

import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Created by bedux on 29/03/16.
 */
public class QueryList {

    private static QueryList instance = new QueryList();
    public final QueryWithPath countAllMethodByFilePath = new QueryWithPath("select COUNT(*) from JavaMethod where JavaMethod.JavaSource  in (select JavaSourceObject.id from JavaSourceObject,JavaFile where JavaSourceObject.javaFile = JavaFile.id AND JavaFile.path = ?);", 1);
    public final QueryWithPath countAllFieldsByFilePath = new QueryWithPath("select COUNT(*) from JavaField where javaSource in  (select id from JavaSourceObject where JavaSourceObject.javaFile in (select id from JavaFile where JavaFile.path = ? ))", 1);


    /***
     * Counting all the javaDoc of class and interface of a specific FilePath
     */
    public final QueryWithPath countAllJavaDocInClassInterfaceByFilePath = new QueryWithPath("select COUNT(*) from JavaDoc where JavaDoc.ContainsTransverseInformation in  (select id from JavaMethod where JavaMethod.JavaSource  in (select JavaSourceObject.id from JavaSourceObject,JavaFile where JavaSourceObject.javaFile = JavaFile.id AND JavaFile.path = ?));", 1);




    private QueryList() {

    }

    public static QueryList getInstance() {
        return instance;
    }

    public synchronized  <T> Optional<T> getById(int id, Class<T> element) throws IllegalAccessException, SQLException, InstantiationException {
        List<T> elementList =  DatabaseManager.getInstance().makeQuery("SELECT * FROM " + element.getAnnotation(IDatabaseClass.class).tableName() + " WHERE id = " + id, new HashMap<>(), element);
        if(elementList.size()>0){
            return Optional.of(elementList.get(0));
        }else{
            return Optional.empty();
        }
    }

    public  synchronized <T> Optional<T> getById(int id, Class<T> element, String idName) throws IllegalAccessException, SQLException, InstantiationException {
        List<T> elementList =  DatabaseManager.getInstance().makeQuery("SELECT * FROM " + element.getAnnotation(IDatabaseClass.class).tableName() + " WHERE " + idName + " = " + id, new HashMap<>(), element);
        if(elementList.size()>0){
            return Optional.of(elementList.get(0));
        }else{
            return Optional.empty();
        }

    }

    public  synchronized List<File> getFileByRepositoryVersion(int repoVersion) throws IllegalAccessException, SQLException, InstantiationException {
        String query = "SELECT * FROM File WHERE repositoryVersionId = ?";
        return DatabaseManager.getInstance().makeQuery(query, new HashMap<Integer, Object>() {{
            put(1, repoVersion);
        }}, File.class);

    }

    public  synchronized Optional<RepositoryVersion> getRepositoryVersionById(int idRepositoryVersion) throws IllegalAccessException, SQLException, InstantiationException {
        String query = "SELECT * FROM RepositoryVersion WHERE id = ?";
        List<RepositoryVersion> elementList =  DatabaseManager.getInstance().makeQuery(query, new HashMap<Integer, Object>() {{
            put(1, idRepositoryVersion);
        }}, RepositoryVersion.class);
        if(elementList.size()>0){
            return Optional.of(elementList.get(0));
        }else{
            return Optional.empty();
        }

    }

    public  synchronized Optional<JavaFile> getJavaFileById(int idJavaFileId) throws IllegalAccessException, SQLException, InstantiationException {
        String query = "SELECT * FROM JavaFile WHERE id = ?";
        List<JavaFile> elementList =  DatabaseManager.getInstance().makeQuery(query, new HashMap<Integer, Object>() {{
            put(1, idJavaFileId);
        }}, JavaFile.class);

        if(elementList.size()>0){
            return Optional.of(elementList.get(0));
        }else{
            return Optional.empty();
        }

    }

    public  synchronized Optional<JavaFile> getJavaFileByPath(String path) throws IllegalAccessException, SQLException, InstantiationException {
        String query = "SELECT * FROM JavaFile WHERE path = ?";

        List<JavaFile> elementList = DatabaseManager.getInstance().makeQuery(query, new HashMap<Integer, Object>() {{
            put(1, path);
        }}, JavaFile.class);
        if(elementList.size()>0){
            return Optional.of(elementList.get(0));
        }else{
            return Optional.empty();
        }

    }

    public  synchronized Optional<TextFile> getTextFileByPath(String path) throws IllegalAccessException, SQLException, InstantiationException {
        String query = "SELECT * FROM TextFile WHERE path = ?";
        List<TextFile> elementList = DatabaseManager.getInstance().makeQuery(query, new HashMap<Integer, Object>() {{
            put(1, path);
        }}, TextFile.class);
        if(elementList.size()>0){
            return Optional.of(elementList.get(0));
        }else{
            return Optional.empty();
        }

    }


    public  synchronized long getTotalSize(int id) throws SQLException, IOException {
        return Files.walk(new java.io.File(Definitions.repositoryPath + id).toPath())
                .filter(path -> !Files.isDirectory(path)).mapToLong(x -> x.toFile().length()).sum();

    }

    public synchronized  long getTotalNumberOfCodeLines(int id) throws SQLException {
        String query = "select sum(CAST(information->>'noLine' AS integer)) from JavaFile where JavaFile.repositoryVersionId = ?";
        List<CountQuery> javaSize = DatabaseManager.getInstance().makeQuery(query, new HashMap<Integer, Object>() {{
            put(1, id);
        }}, CountQuery.class);

        if(javaSize.size()>0){return javaSize.get(0).count;}else{return 0;}

    }

    public synchronized  String getProjectName(int version) throws SQLException {
        String query = "select * from Repository,RepositoryVersion where Repository.id = ?";
        List<Repository> repo = DatabaseManager.getInstance().makeQuery(query, new HashMap<Integer, Object>() {{
            put(1, version);
        }}, Repository.class);
        if(repo.size()>0){
            return repo.get(0).url;
        }else{
            return "";
        }

    }

    /***
     * Given a repository version, return the number of the file
     *
     * @param version Repository Version @see RepositoryVersion
     * @return the number of files(java file + text file + binary file)
     * @throws SQLException
     */
    public  synchronized long getNumberOfFile(int version) throws SQLException {
        String query = "select COUNT(*) from File where File.RepositoryVersionId = ?";
        long repo = DatabaseManager.getInstance().makeQuery(query, new HashMap<Integer, Object>() {{
            put(1, version);
        }}, CountQuery.class).get(0).count;
        return repo;
    }

    /***
     * Make a query that return the maximum number of field in a single class.
     *
     * @param id repository Version id
     * @return the maximum field
     * @throws SQLException
     */
    public  synchronized long getMaximumJavaFields(int id) throws SQLException {
        String query = "select COUNT(JavaClass) from JavaFile,JavaClass,JavaField where ? = JavaFile.repositoryVersionId AND JavaFile.id = JavaClass.JavaFile AND JavaClass.id = JavaField.JavaSource GROUP BY(JavaClass) ORDER BY COUNT(*) DESC LIMIT 1;";
        long javaSize = DatabaseManager.getInstance().makeQuery(query, new HashMap<Integer, Object>() {{
            put(1, id);
        }}, CountQuery.class).get(0).count;
        return javaSize;

    }

    /***
     * Make a query that return the maximum number of methods in a single class.
     *
     * @param id repository Version id
     * @return the maximum methods
     * @throws SQLException
     */
    public  synchronized long getMaximumJavaMethods(int id) throws SQLException {
        String query = "select COUNT(JavaClass) from JavaFile,JavaClass,JavaMethod where ? = JavaFile.repositoryVersionId AND JavaFile.id = JavaClass.JavaFile AND JavaClass.id = JavaMethod.JavaSource GROUP BY(JavaClass) ORDER BY COUNT(*) DESC LIMIT 1;";
        long javaSize = DatabaseManager.getInstance().makeQuery(query, new HashMap<Integer, Object>() {{
            put(1, id);
        }}, CountQuery.class).get(0).count;
        return javaSize;

    }

    /***
     * Make a quey that return the number of java doc for the method of the given file path. He doesn't count the constructor!!!
     *
     * @param path
     * @return the number of javaDoc
     * @throws SQLException
     */
    public synchronized  long countJavaDocMethodsByPath(String path) throws SQLException {
        String query = "select Count(*) from JavaDoc where JavaDoc.ContainsTransverseInformation in  (select id from JavaMethod where JavaMethod.JavaSource   in (select JavaClass.id from JavaClass,JavaFile where JavaClass.javaFile = JavaFile.id AND JavaFile.path = ? ));";
        long javaSize = DatabaseManager.getInstance().makeQuery(query, new HashMap<Integer, Object>() {{
            put(1, path);
        }}, CountQuery.class).get(0).count;
        return javaSize;
    }


    public  synchronized List<JavaClass> getAllJavaClassByJavaFile(long id) throws SQLException {
        String query = "select * from JavaClass where JavaClass.JavaFile = ?";
        List<JavaClass> repo = DatabaseManager.getInstance().makeQuery(query, new HashMap<Integer, Object>() {{
            put(1, id);
        }}, JavaClass.class);
        return repo;
    }

    public synchronized  List<JavaInterface> getAllJavaInterfaceByJavaFile(long id) throws SQLException {
        String query = "select * from JavaInterface where JavaInterface.JavaFile = ?";
        List<JavaInterface> repo = DatabaseManager.getInstance().makeQuery(query, new HashMap<Integer, Object>() {{
            put(1, id);
        }}, JavaInterface.class);
        return repo;
    }


    /***
     * Given a javaFile. return a list of his JavaImport
     *
     * @param javaFileId id of the file
     * @return a list of the import
     * @throws SQLException
     */
    public  synchronized List<JavaImport> getAllJavaImportFromJavaFile(long javaFileId) throws SQLException {
        String query =
                "select * from JavaImport where JavaImport.javafile = ?;";
        List<JavaImport> importDiscussion = DatabaseManager.getInstance().makeQuery(query, new HashMap<Integer, Object>() {{
            put(1, javaFileId);
        }}, JavaImport.class);
        return importDiscussion;
    }


    /***
     * return a list of ImportDiscussions
     *
     * @return a list of the import
     * @throws SQLException
     */
    public synchronized  List<ImportDiscussion> gelAllImportFromDiscussion() throws SQLException {
        String query =
                "select * from import;";
        List<ImportDiscussion> importDiscussion = DatabaseManager.getInstance().makeQuery(query, new HashMap<Integer, Object>(), ImportDiscussion.class);
        return importDiscussion;
    }


    public synchronized  List<JavaPackage> getAllPackages(long id) throws SQLException {
        String query =
                "select * from JavaPackage where JavaPackage.javaFIle in (select id from JavaFile where repositoryVersionId = ?);";
        List<JavaPackage> importDiscussion = DatabaseManager.getInstance().makeQuery(query, new HashMap<Integer, Object>() {{
            put(1, id);
        }}, JavaPackage.class);
        return importDiscussion;
    }


    public synchronized  List<JavaImport> getAllNonLocalImport(long id, long repoVersionId) throws SQLException {
        List<JavaImport> javaImports = QueryList.getInstance().getAllJavaImportFromJavaFile(id);
        List<JavaPackage> packages = QueryList.getInstance().getAllPackages(repoVersionId);
        return javaImports.parallelStream().filter(x -> {
                    Boolean bb = !packages.parallelStream().anyMatch(y -> {
                        Boolean result = x.json.name.contains(y.json.name);
                        // if(result){
                        // }
                        return result;
                    });
                    return bb;

                }
        ).collect(Collectors.toList());
    }

    public  synchronized List<JavaImport> getAllDiscussedJavaImport(List<JavaImport> javaImport) throws SQLException {
        List<ImportDiscussion> importDsicussions = QueryList.getInstance().gelAllImportFromDiscussion();
        return javaImport.parallelStream().filter(x -> {
            long s = importDsicussions.parallelStream().filter(y -> {
                        String sToTest = y.packageDiscussion.replace(".*", "");
                        return x.json.name.indexOf(sToTest) != -1;
                    }
            ).count();
            return s > 0;
        }).collect(Collectors.toList());
    }

    public synchronized  List<ImportDiscussion> getAllDiscussionImport(List<JavaImport> javaImport) throws SQLException {
        List<ImportDiscussion> importDiscussions = QueryList.getInstance().gelAllImportFromDiscussion();
        return importDiscussions.parallelStream().filter(y -> {
            long s = javaImport.parallelStream().filter(x -> {
                        String sToTest = y.packageDiscussion.replace(".*", "");
                        return x.json.name.indexOf(sToTest) != -1;
                    }
            ).count();
            return s > 0;
        }).collect(Collectors.toList());

    }


    public synchronized  List<StackOFDiscussion> getGitDiscussionFromImportDiscussion(List<ImportDiscussion> importsDiscussions) throws SQLException{
            String query = "select * from discussion where discussion.id in (select import_discussion.idd from import_discussion where import_discussion.idi = ?);";
            List<StackOFDiscussion> result  = new ArrayList<>();
            for(ImportDiscussion s:importsDiscussions) {
                result.addAll(DatabaseManager.getInstance().makeQuery(query, new HashMap<Integer, Object>() {{
                    put(1, s.id);
                }}, StackOFDiscussion.class));

            }
            return result;

    }


    /***
     * Get all gava method from path
     * @param path
     * @return
     * @throws SQLException
     */
    public synchronized  List<String> getAllJavaMethodFormPath(String path) throws SQLException {

        String query = "select * from JavaMethod where javasource in (select id from javaClass where javaFile in (select id from JavaFile where path = ? ))";
        List<String> result = DatabaseManager.getInstance().makeQuery(query, new HashMap<Integer, Object>() {{
            put(1, path);
        }}, JavaMethod.class).stream().flatMap(x->x.json.variableDeclaration.stream()).collect(Collectors.toList());

        //Get all method invocation inside a Class declaration
        String query2 = "select * from javaClass where javaFile in (select id from JavaFile where path = ? )";
        List<JavaClass> methodClass = DatabaseManager.getInstance().makeQuery(query2, new HashMap<Integer, Object>() {{
            put(1, path);
        }}, JavaClass.class);
        result.addAll(methodClass.stream().flatMap(x->x.json.variableDeclaration.stream()).collect(Collectors.toList()));

        String query3 = "select * from JavaInterface where javaFile in (select id from JavaFile where path = ? )";

        //Get all method invocation inside an Interface
        List<JavaInterface> interfClass = DatabaseManager.getInstance().makeQuery(query3, new HashMap<Integer, Object>() {{
            put(1, path);
        }}, JavaInterface.class);
        result.addAll(interfClass.stream().flatMap(x->x.json.variableDeclaration.stream()).collect(Collectors.toList()));


        return result;
    }

    /***
     * Get all the discussion that has at least one  method that match with the import list
     * @param methodsName
     * @return
     * @throws SQLException
     */

    public synchronized  List<StackOFDiscussion> getAllDiscussionHavingMethodName(List<String> methodsName) throws SQLException{
        if(methodsName.size()==0) return  new ArrayList<>();
        methodsName = methodsName.stream().distinct().collect(Collectors.toList());
        String param = "(";
        for(String r:methodsName){
              param=param+"'"+r+"',";
        }
       final String rparam =  param.substring(0,param.length()-1)+")";
        String query = "select * from Discussion where id in (select idd from method_discussion where idm in (select id from method where methodname in "+rparam+") )";

        List<StackOFDiscussion> importDiscussion = DatabaseManager.getInstance().makeQuery(query, new HashMap<Integer, Object>()
        , StackOFDiscussion.class);
        return importDiscussion;
    }


    /***
     * Get all java method invocation from a repository version id
     * @param id
     * @return
     * @throws SQLException
     */
    public synchronized  List<String> getAllJavaMethodOfRepositoryVersion(long id) throws SQLException{
        //Get all method invoation inside a method
        String query = "select * from JavaMethod where javasource in (select id from javaClass where javaFile in (select id from JavaFile where repositoryVersionId = ? ));";
        List<JavaMethod> importDiscussion = DatabaseManager.getInstance().makeQuery(query, new HashMap<Integer, Object>() {{
            put(1, id);
        }}, JavaMethod.class);
        List<String> result = importDiscussion.stream().map(x -> x.json.signature).collect(Collectors.toList());

        result = result.stream().collect(Collectors.toList());

        return result;

    }

    /***
     * Given a list of method name, retun the discussions that use this methods
     * @param methodsName
     * @return A list of MethodDiscussion
     * @throws SQLException
     */


    public  synchronized List<MethodDiscussed> getAllMethodDiscussed(List<String> methodsName) throws SQLException{
        if(methodsName.size()==0) return  new ArrayList<>();
        methodsName = methodsName.stream().distinct().collect(Collectors.toList());
        String param = "(";
        for(String r:methodsName){
            param=param+"'"+r+"',";
        }
        final String rparam =  param.substring(0,param.length()-1)+")";
        String query = "select  distinct on (methodname) * from method where methodname in "+rparam+"";
        List<MethodDiscussed> importDiscussion = DatabaseManager.getInstance().makeQuery(query, new HashMap<Integer, Object>()
                , MethodDiscussed.class);
        return importDiscussion;

    }


}

