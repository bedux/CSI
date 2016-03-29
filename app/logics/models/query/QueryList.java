package logics.models.query;

import logics.DatabaseManager;
import logics.databaseUtilities.IDatabaseClass;
import logics.models.db.File;
import logics.models.db.JavaFile;
import logics.models.db.RepositoryVersion;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by bedux on 29/03/16.
 */
public class QueryList {

    private static QueryList instance = null;
    public final QueryWithPath countAllMethodByFilePath = new QueryWithPath("select COUNT(*) from JavaMethod where javaSource in  (select id_JSO from JavaSourceObject where JavaSourceObject.javaFile in (select id_F from JavaFile where JavaFile.path = ? )LIMIT 1)", 1);
    public final QueryWithPath countAllFieldsByFilePath = new QueryWithPath("select COUNT(*) from JavaField where javaSource in  (select id_JSO from JavaSourceObject where JavaSourceObject.javaFile in (select id_F from JavaFile where JavaFile.path = ? )LIMIT 1)", 1);
    /***
     * Counting all the javaDoc of class and interface of a specific FilePath
     */
    public final QueryWithPath countAllJavaDocInClassInterfaceByFilePath = new QueryWithPath("select COUNT(*) from JavaDoc where JavaDoc.containstransversalinformation in  (select id from JavaSourceObjects where JavaSourceObjects.javaFile in (select id from JavaFile where JavaFile.path = ?)LIMIT 1)", 1);

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
    public <T> T getById(int id, Class<T> element,String idName) throws IllegalAccessException, SQLException, InstantiationException {
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
        String query = "SELECT * FROM JavaFile WHERE id_JF = ?";
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
//    public final QueryWithPath countAllJavaDocInMethodFieldsByFilePath =new QueryWithPath(,1);

}
