package logics.models.modelQuery;

import logics.databaseCache.DatabaseModels;
import logics.models.db.File;

/**
 * Created by bedux on 29/04/16.
 */
public class NumberOfFile implements IQuery<Long,Long> {
    @Override
    public Long execute(Long param) {
        return DatabaseModels.getInstance().getAll(File.class).stream().filter(x->x.getRepositoryVersionConcrete().getId()==param).count();
    }

    @Override
    public IQuery<Long, Long> clone() {
        return new NumberOfFile();
    }
}
