package logics.models.query;

import logics.databaseUtilities.QueryContainer;
import logics.models.db.RepositoryVersion;

import java.util.HashMap;

/**
 * Created by bedux on 26/03/16.
 */
public class QueryGetAllRepository extends QueryContainer<RepositoryVersion> {

    private final String query = "select * from RepositoryVersion";

    public QueryGetAllRepository() {
        super("select * from RepositoryVersion", new HashMap<>(), RepositoryVersion.class);
    }


}
