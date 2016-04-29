package logics.models.modelQuery;

import logics.databaseCache.DatabaseModels;
import logics.models.db.RepositoryRender;

import java.util.List;
import java.util.Queue;

/**
 * Created by bedux on 29/04/16.
 */
public class AllRepositoryVersionRender implements IQuery<Void,List<RepositoryRender>> {
    @Override
    public List<RepositoryRender> execute(Void param) {
        return DatabaseModels.getInstance().getAll(RepositoryRender.class);

    }

    @Override
    public IQuery<Void, List<RepositoryRender>> clone() {
        return new AllRepositoryVersionRender();
    }
}
