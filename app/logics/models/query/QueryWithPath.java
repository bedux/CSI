package logics.models.query;

import logics.databaseUtilities.QueryContainer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by bedux on 26/03/16.
 */
public class QueryWithPath extends QueryContainer<CountQuery>{

    private final List<Integer> pathId = new ArrayList<>();

    public QueryWithPath(String query, HashMap<Integer, Object> params, int pathId) {
        super(query, params, CountQuery.class);
        this.pathId.add(pathId);

    }

    public QueryWithPath(String query, int pathId) {
        super(query, CountQuery.class);
        this.pathId.add(pathId);
    }
    public QueryWithPath(String query,  List<Integer> pathId) {
        super(query, CountQuery.class);
        this.pathId.addAll(pathId);
    }




    public Object getPath() {
        return (String) params.get(pathId);
    }

    public void setPath(Object path) {
        for(int p:pathId) {
            params.put(p, path);
        }
    }

    public QueryWithPath clone(){
        QueryWithPath inst =  new QueryWithPath(query,pathId);
        return inst;
    }




}
