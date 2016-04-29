package logics.models.modelQuery;

import logics.databaseCache.DatabaseModels;
import logics.models.db.JavaFile;

/**
 * Created by bedux on 29/04/16.
 */
public  class TotalNumberOfCodeLines implements IQuery<Long,Long> {
    @Override
    public Long execute(Long id) {
        return (long) (DatabaseModels.getInstance().getAll(JavaFile.class).stream().filter(x->x.getRepositoryVersionConcrete().getId()==id).map(x->x.getJson().noLine)
                .reduce((x,y)->x+y).get());
    }

    @Override
    public IQuery<Long, Long> clone() {
        return new TotalNumberOfCodeLines();
    }
}
