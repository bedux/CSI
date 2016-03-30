package logics.databaseUtilities;

import exception.CustomException;
import logics.DatabaseManager;

import java.util.HashMap;
import java.util.List;


public class QueryContainer<T> {
    protected Class<T> resultClass;
    protected HashMap<Integer, Object> params = new HashMap<>();
    private String query;

    /***
     *
     *
     *
     * @param query String of the query to be run
     * @param params The params of the query
     * @param resultClass The type of the resulting class
     */
    public QueryContainer(String query, HashMap<Integer, Object> params, Class<T> resultClass) {
        this.query = query;
        this.params = params;
        this.resultClass = resultClass;
    }

    /***
     * Query Whithout parameters
     * @param query String of the query to be run
     * @param resultClass The type of the resulting class
     */
    public QueryContainer(String query, Class<T> resultClass) {
        this.query = query;
        this.resultClass = resultClass;

    }

    /***
     *
     * @return return a list of The type of the query table.
     */
    public List<T> execute() {
        try {
            return DatabaseManager.getInstance().makeQuery(query, params, resultClass);
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }

    /***
     *
     * @return the first element of the list of result.
     */
    public T executeAndGetFirst() {
        try {
            return DatabaseManager.getInstance().makeQuery(query, params, resultClass).get(0);
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }
}
