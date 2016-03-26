package logics.models.query;

import logics.databaseUtilities.QueryContainer;

import java.util.HashMap;

/**
 * Created by bedux on 26/03/16.
 */
public class QueryWithPath extends QueryContainer<Count> {

    int pathId;
    public String getPath() {
       return (String) params.get(pathId);
    }

    public void setPath(String path) {
        this.path = path;
        params.put(pathId,path);
    }

    private String path;

    public QueryWithPath(String query, HashMap<Integer, Object> params,int pathId) {
        super(query, params, Count.class);
        this.pathId = pathId;

    }

    public QueryWithPath(String query,int pathId) {
        super(query, Count.class);
        this.pathId = pathId;
    }



}
