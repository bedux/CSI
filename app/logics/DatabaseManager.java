package logics;
import logics.databaseUtilities.ConvertTableToClass;
import logics.databaseUtilities.IDatabaseClass;
import logics.models.db.File;
import logics.models.db.JavaFile;
import logics.models.db.RepositoryVersion;
import play.db.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DatabaseManager{

    private static DatabaseManager instance = null;
    private DatabaseManager(){

    }

    public static DatabaseManager getInstance(){
        if(instance==null){
            instance= new DatabaseManager();
        }
        return instance;

    }


    /**
     * Make a generic query
     * makeQuery("SELECT * FROM JavaFile WHERE id = ? ",new HashMap<Integer,Object>(){{put(1,1);}},JavaFile.class)
     * @param query the query's string
     * @param values an hash map that contains all the params
     * @param resultType class of result
     * @param <T> type of result
     * @return a list of result
     * @throws SQLException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public <T> ArrayList<T> makeQuery(String query,HashMap<Integer,Object> values,Class<T> resultType) throws SQLException, InstantiationException, IllegalAccessException {

            Connection connection = DB.getConnection();
            PreparedStatement preparedStatement = buildPreparedStatement(connection,query, values);


            ArrayList<T> returnedValue = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                T result = new ConvertTableToClass(resultSet).convert(resultType);
                returnedValue.add(result);
                }
            preparedStatement.close();
            resultSet.close();
             connection.close();
        return returnedValue;
    }



    /**
     *
     * @param query
     * @param values
     * @return
     * @throws SQLException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public boolean makeUpdateQuery(String query,HashMap<Integer,Object> values) throws SQLException, InstantiationException, IllegalAccessException {
        Connection connection = DB.getConnection();

        PreparedStatement preparedStatement = buildPreparedStatement(connection,query, values);

        boolean result = preparedStatement.execute();
        preparedStatement.close();
        connection.close();

        return result;
    }

    public int makeSaveQuery(String query,HashMap<Integer,Object> values) throws SQLException, InstantiationException, IllegalAccessException {
        Connection connection = DB.getConnection();
        PreparedStatement preparedStatement = buildPreparedStatement(connection,query, values);
        ResultSet r = preparedStatement.executeQuery();
        r.next();
        int result = r.getInt(1);
        preparedStatement.close();

        connection.close();
        return result;
    }

    private PreparedStatement buildPreparedStatement(Connection connection,String query, HashMap<Integer, Object> values) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement(query);

        for (HashMap.Entry<Integer, Object> mapEntry : values.entrySet()) {
            preparedStatement.setObject(mapEntry.getKey(), mapEntry.getValue());

        }

        return preparedStatement;
    }

    public<T> T getById(int id,Class<T> element) throws IllegalAccessException, SQLException, InstantiationException {
       return makeQuery("SELECT * FROM " + element.getAnnotation(IDatabaseClass.class).tableName() +" WHERE id = "+id,new HashMap<>(),element).get(0);


    }

    public List<File> getFileByRepositoryVersion(int repoVersion) throws IllegalAccessException, SQLException, InstantiationException {
        String query = "SELECT * FROM File WHERE repositoryVersionId = ?";
        return makeQuery(query,new HashMap<Integer,Object>(){{put(1,repoVersion);}},File.class);

    }

    public RepositoryVersion getRepositoryVersionById(int idRepositoryVersion) throws IllegalAccessException, SQLException, InstantiationException {
        String query = "SELECT * FROM RepositoryVersion WHERE id = ?";
        return makeQuery(query,new HashMap<Integer,Object>(){{put(1,idRepositoryVersion);}},RepositoryVersion.class).get(0);

    }
    public JavaFile getJavaFileById(int idJavaFileId) throws IllegalAccessException, SQLException, InstantiationException {
        String query = "SELECT * FROM JavaFile WHERE id = ?";
        return makeQuery(query,new HashMap<Integer,Object>(){{put(1,idJavaFileId);}},JavaFile.class).get(0);

    }

    public JavaFile getJavaFileByPath(String path) throws IllegalAccessException, SQLException, InstantiationException {
        String query = "SELECT * FROM JavaFile WHERE path = ?";
        return makeQuery(query,new HashMap<Integer,Object>(){{put(1,path);}},JavaFile.class).get(0);

    }

}





