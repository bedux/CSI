package logics;

import exception.CustomException;
import logics.databaseUtilities.ConvertTableToClass;
import play.db.DB;
import play.libs.Json;
import scala.util.parsing.json.JSONObject$;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;


public class DatabaseManager {

    private static   DatabaseManager instance = new DatabaseManager();;

    private DatabaseManager() {

    }

    public static DatabaseManager getInstance() {
        return instance;
    }


    /**
     * Make a generic query
     * makeQuery("SELECT * FROM JavaFile WHERE id = ? ",new HashMap<Integer,Object>(){{put(1,1);}},JavaFile.class)
     *
     * @param query      the query's string
     * @param values     an hash map that contains all the params
     * @param resultType class of result
     * @param <T>        type of result
     * @return a list of result
     * @throws SQLException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public synchronized <T> ArrayList<T> makeQuery(String query, HashMap<Integer, Object> values, Class<T> resultType) throws SQLException {

        Connection connection = DB.getConnection();
        try {
            PreparedStatement preparedStatement = buildPreparedStatement(connection, query, values);
//            System.out.println();
//            System.out.print(preparedStatement.toString()+Json.stringify(Json.toJson(values)));
//            System.out.println();

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
        }catch (Exception e){
            connection.close();
            throw new  CustomException(e);
        }
    }


    /**
     * @param query
     * @param values
     * @return
     * @throws SQLException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public synchronized boolean makeUpdateQuery(String query, HashMap<Integer, Object> values) throws SQLException {
        Connection connection = DB.getConnection();
        try {

            PreparedStatement preparedStatement = buildPreparedStatement(connection, query, values);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();

            return true;
        }catch (Exception e){
            connection.close();
            throw new CustomException(e);
        }
    }

    /**
     * Make a save query
     * @param query make the current query
     * @param values apply the following parameters
     * @return the id of the saved element
     * @throws SQLException
     */
    public synchronized int makeSaveQuery(String query, HashMap<Integer, Object> values) throws SQLException {
        Connection connection = DB.getConnection();
       try {
           PreparedStatement preparedStatement = buildPreparedStatement(connection, query, values);

           ResultSet r = preparedStatement.executeQuery();
           r.next();
           int result = r.getInt(1);
           preparedStatement.close();

           connection.close();
           return result;
       }catch (Exception e){
           connection.close();
           throw new CustomException(e);
       }

    }


    /**
     *
     * @param connection the current connection
     * @param query the current query
     * @param values the current params
     * @return the PreparedStatement with the new values
     * @throws SQLException
     */
    private synchronized PreparedStatement buildPreparedStatement(Connection connection, String query, HashMap<Integer, Object> values) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement(query);

        for (HashMap.Entry<Integer, Object> mapEntry : values.entrySet()) {
            preparedStatement.setObject(mapEntry.getKey(), mapEntry.getValue());

        }

        return preparedStatement;
    }


}





