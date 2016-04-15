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
import java.util.stream.Collectors;


/**
 * Created by bedux on 29/03/16.
 */
public class QueryList {

    private static QueryList instance = null;
    public final QueryWithPath countAllMethodByFilePath = new QueryWithPath("select COUNT(*) from JavaMethod where JavaMethod.JavaSource  in (select JavaSourceObject.id from JavaSourceObject,JavaFile where JavaSourceObject.javaFile = JavaFile.id AND JavaFile.path = ?);", 1);
    public final QueryWithPath countAllFieldsByFilePath = new QueryWithPath("select COUNT(*) from JavaField where javaSource in  (select id from JavaSourceObject where JavaSourceObject.javaFile in (select id from JavaFile where JavaFile.path = ? ))", 1);
    public final QueryWithPath getAllNonProjectImport = new QueryWithPath("select COUNT(*) from javaImport where (javaImport.information ->>'name') not in (select DISTINCT javaImport.information ->>'name'  from javaImport,javaPackage where (javaImport.information ->>'name' ~~ (javaPackage.information ->>'name'  || '%')) AND javaImport.javafile = ? ) and javaImport.javafile = ?;", new ArrayList<Integer>() {{
        add(1);
        add(2);
    }});
    public final QueryWithPath countDiscussionAndActualImport = new QueryWithPath("select COUNT(*) from import where import.package in (select javaImport.information ->>'name' from javaImport where (javaImport.information ->>'name') not in (select DISTINCT javaImport.information ->>'name'  from javaImport,javaPackage where (javaImport.information ->>'name' ~~ (javaPackage.information ->>'name'  || '%')) AND javaImport.javafile = ? ) and javaImport.javafile = ?);", new ArrayList<Integer>() {{
        add(1);
        add(2);
    }});
    public final ComputeProportionOfTwoQueryById ratioImportDiscussion = new ComputeProportionOfTwoQueryById(new ComputeWithSingleQuery(getAllNonProjectImport), new ComputeWithSingleQuery(countDiscussionAndActualImport));


    /***
     * Counting all the javaDoc of class and interface of a specific FilePath
     */
    public final QueryWithPath countAllJavaDocInClassInterfaceByFilePath = new QueryWithPath("select COUNT(*) from JavaDoc where JavaDoc.ContainsTransverseInformation in  (select id from JavaMethod where JavaMethod.JavaSource  in (select JavaSourceObject.id from JavaSourceObject,JavaFile where JavaSourceObject.javaFile = JavaFile.id AND JavaFile.path = ?));", 1);


    public final ComputeProportionOfTwoQuery ratioJavaDocMethodsByPath = new ComputeProportionOfTwoQuery(new ComputeWithSingleQuery(countAllMethodByFilePath), new ComputeWithSingleQuery(countAllJavaDocInClassInterfaceByFilePath));


    private QueryList() {

    }

    public static QueryList getInstance() {
        if (instance == null) {
            instance = new QueryList();
        }
        return instance;
    }

    public <T> T getById(int id, Class<T> element) throws IllegalAccessException, SQLException, InstantiationException {
        return DatabaseManager.getInstance().makeQuery("SELECT * FROM " + element.getAnnotation(IDatabaseClass.class).tableName() + " WHERE id = " + id, new HashMap<>(), element).get(0);
    }

    public <T> T getById(int id, Class<T> element, String idName) throws IllegalAccessException, SQLException, InstantiationException {
        return DatabaseManager.getInstance().makeQuery("SELECT * FROM " + element.getAnnotation(IDatabaseClass.class).tableName() + " WHERE " + idName + " = " + id, new HashMap<>(), element).get(0);
    }

    public List<File> getFileByRepositoryVersion(int repoVersion) throws IllegalAccessException, SQLException, InstantiationException {
        String query = "SELECT * FROM File WHERE repositoryVersionId = ?";
        return DatabaseManager.getInstance().makeQuery(query, new HashMap<Integer, Object>() {{
            put(1, repoVersion);
        }}, File.class);

    }

    public RepositoryVersion getRepositoryVersionById(int idRepositoryVersion) throws IllegalAccessException, SQLException, InstantiationException {
        String query = "SELECT * FROM RepositoryVersion WHERE id = ?";
        return DatabaseManager.getInstance().makeQuery(query, new HashMap<Integer, Object>() {{
            put(1, idRepositoryVersion);
        }}, RepositoryVersion.class).get(0);

    }

    public JavaFile getJavaFileById(int idJavaFileId) throws IllegalAccessException, SQLException, InstantiationException {
        String query = "SELECT * FROM JavaFile WHERE id = ?";
        return DatabaseManager.getInstance().makeQuery(query, new HashMap<Integer, Object>() {{
            put(1, idJavaFileId);
        }}, JavaFile.class).get(0);

    }

    public JavaFile getJavaFileByPath(String path) throws IllegalAccessException, SQLException, InstantiationException {
        String query = "SELECT * FROM JavaFile WHERE path = ?";

        return DatabaseManager.getInstance().makeQuery(query, new HashMap<Integer, Object>() {{
            put(1, path);
        }}, JavaFile.class).get(0);

    }

    public TextFile getTextFileByPath(String path) throws IllegalAccessException, SQLException, InstantiationException {
        String query = "SELECT * FROM TextFile WHERE path = ?";
        return DatabaseManager.getInstance().makeQuery(query, new HashMap<Integer, Object>() {{
            put(1, path);
        }}, TextFile.class).get(0);

    }


    public long getTotalSize(int id) throws SQLException, IOException {
        return Files.walk(new java.io.File(Definitions.repositoryPath + id).toPath())
                .filter(path -> !Files.isDirectory(path)).mapToLong(x -> x.toFile().length()).sum();

    }

    public long getTotalNumberOfCodeLines(int id) throws SQLException {
        String query = "select sum(CAST(information->>'noLine' AS integer)) from JavaFile where JavaFile.repositoryVersionId = ?";
        long javaSize = DatabaseManager.getInstance().makeQuery(query, new HashMap<Integer, Object>() {{
            put(1, id);
        }}, CountQuery.class).get(0).count;
        return javaSize;
    }

    public String getProjectName(int version) throws SQLException {
        String query = "select * from Repository,RepositoryVersion where Repository.id = ?";
        Repository repo = DatabaseManager.getInstance().makeQuery(query, new HashMap<Integer, Object>() {{
            put(1, version);
        }}, Repository.class).get(0);
        return repo.url;
    }

    /***
     * Given a repository version, return the number of the file
     *
     * @param version Repository Version @see RepositoryVersion
     * @return the number of files(java file + text file + binary file)
     * @throws SQLException
     */
    public long getNumberOfFile(int version) throws SQLException {
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
    public long getMaximumJavaFields(int id) throws SQLException {
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
    public long getMaximumJavaMethods(int id) throws SQLException {
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
    public long countJavaDocMethodsByPath(String path) throws SQLException {
        String query = "select Count(*) from JavaDoc where JavaDoc.ContainsTransverseInformation in  (select id from JavaMethod where JavaMethod.JavaSource   in (select JavaClass.id from JavaClass,JavaFile where JavaClass.javaFile = JavaFile.id AND JavaFile.path = ? ));";
        long javaSize = DatabaseManager.getInstance().makeQuery(query, new HashMap<Integer, Object>() {{
            put(1, path);
        }}, CountQuery.class).get(0).count;
        return javaSize;
    }


    public List<JavaClass> getAllJavaClassByJavaFile(long id) throws SQLException {
        String query = "select * from JavaClass where JavaClass.JavaFile = ?";
        List<JavaClass> repo = DatabaseManager.getInstance().makeQuery(query, new HashMap<Integer, Object>() {{
            put(1, id);
        }}, JavaClass.class);
        return repo;
    }

    public List<JavaInterface> getAllJavaInterfaceByJavaFile(long id) throws SQLException {
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
    public List<JavaImport> getAllJavaImportFromJavaFile(long javaFileId) throws SQLException {
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
    public List<ImportDiscussion> gelAllImportFromDiscussion() throws SQLException {
        String query =
                "select * from import;";
        List<ImportDiscussion> importDiscussion = DatabaseManager.getInstance().makeQuery(query, new HashMap<Integer, Object>(), ImportDiscussion.class);
        return importDiscussion;
    }


    public List<JavaPackage> getAllPackages(long id) throws SQLException {
        String query =
                "select * from JavaPackage where JavaPackage.javaFIle in (select id from JavaFile where repositoryVersionId = ?);";
        List<JavaPackage> importDiscussion = DatabaseManager.getInstance().makeQuery(query, new HashMap<Integer, Object>() {{
            put(1, id);
        }}, JavaPackage.class);
        return importDiscussion;
    }


    public List<JavaImport> getAllNonLocalImport(long id, long repoVersionId) throws SQLException {
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

    public List<JavaImport> getAllDiscussedJavaImport(List<JavaImport> javaImport) throws SQLException {
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

    public List<ImportDiscussion> getAllDissussionImport(List<JavaImport> javaImport) throws SQLException {
        List<ImportDiscussion> importDsicussions = QueryList.getInstance().gelAllImportFromDiscussion();
        return importDsicussions.parallelStream().filter(y -> {
            long s = javaImport.parallelStream().filter(x -> {
                        String sToTest = y.packageDiscussion.replace(".*", "");
                        return x.json.name.indexOf(sToTest) != -1;
                    }
            ).count();
            return s > 0;
        }).collect(Collectors.toList());

    }


    public List<StackOFDiscussion> getGitDiscussionFromImportDiscussion(List<ImportDiscussion> importsDiscussions) throws SQLException{
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
    public List<String> getAllJavaMethodFormPath(String path) throws SQLException {

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
        List<JavaInterface> interfClass = DatabaseManager.getInstance().makeQuery(query2, new HashMap<Integer, Object>() {{
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

    public List<StackOFDiscussion> getAllDiscussionHavingMethodName(List<String> methodsName) throws SQLException{
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
    public List<String> getAllJavaMethodOfRepositoryVersion(long id) throws SQLException{
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


    public List<MethodDiscussed> getAllMethodDiscussed(List<String> methodsName) throws SQLException{
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

