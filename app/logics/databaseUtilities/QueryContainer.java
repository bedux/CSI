package logics.databaseUtilities;

import exception.CustomException;
import logics.DatabaseManager;

import java.util.HashMap;
import java.util.List;


public class QueryContainer<T> {
    protected Class<T> resultClass;
    protected HashMap<Integer, Object> params = new HashMap<>();
    private String query;

    public QueryContainer(String query, HashMap<Integer, Object> params, Class<T> resultClass) {
        this.query = query;
        this.params = params;
        this.resultClass = resultClass;
    }

    public QueryContainer(String query, Class<T> resultClass) {
        this.query = query;
        this.resultClass = resultClass;

    }

    public List<T> execute() {
        try {
            return DatabaseManager.getInstance().makeQuery(query, params, resultClass);
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }

    public T executeAndGetFirst() {
        try {
            return DatabaseManager.getInstance().makeQuery(query, params, resultClass).get(0);
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }
}
