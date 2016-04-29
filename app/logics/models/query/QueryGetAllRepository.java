package logics.models.query;

import logics.databaseCache.DatabaseModels;
import logics.databaseUtilities.QueryContainer;
import logics.models.db.RepositoryRender;
import logics.models.db.RepositoryVersion;

import java.util.HashMap;

/**
 * Created by bedux on 26/03/16.
 */
@Deprecated
public class QueryGetAllRepository implements IComputeAttributeContainer {
    @Override
    public long executeAndGetResult(String path) {
            return 1;
    }

    @Override
    public long executeAndGetResult(long id) {
        return 0;
    }

    @Override
    public IComputeAttributeContainer clone() {
        return null;
    }


//    public QueryGetAllRepository() {
//        super("select * from repositoryrender", new HashMap<>(), RepositoryRender.class);
//    }


}
