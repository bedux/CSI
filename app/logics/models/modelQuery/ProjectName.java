package logics.models.modelQuery;

import logics.databaseCache.DatabaseModels;
import logics.models.db.RepositoryVersion;

/**
 * Created by bedux on 29/04/16.
 */
public class ProjectName implements IQuery<Long,String> {
    @Override
    public String execute(Long param) {

        return DatabaseModels.getInstance().getEntity(RepositoryVersion.class,param).get().getRepository().getUrl();
    }

    @Override
    public IQuery<Long, String> clone() {
        return new ProjectName();
    }
}
