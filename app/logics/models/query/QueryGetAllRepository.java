package logics.models.query;

import logics.databaseUtilities.QueryContainer;
import logics.models.db.RepositoryRender;
import logics.models.db.RepositoryVersion;

import java.util.HashMap;

/**
 * Created by bedux on 26/03/16.
 */
public class QueryGetAllRepository extends QueryContainer<RepositoryRender> {


    public QueryGetAllRepository() {
        super("select * from repositoryrender", new HashMap<>(), RepositoryRender.class);
    }


}
