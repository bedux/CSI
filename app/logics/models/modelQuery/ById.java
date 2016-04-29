package logics.models.modelQuery;

import logics.databaseUtilities.Pair;
import logics.databaseCache.DatabaseModels;

import java.util.Optional;

/**
 * Created by bedux on 29/04/16.
 */
public class ById <T> implements IQuery<Pair<Long,Class<T>>,Optional<T>> {
    @Override
    public Optional<T> execute(Pair<Long,Class<T> > param) {
        return DatabaseModels.getInstance().getEntity(param.getValue(),param.getKey());
    }

    @Override
    public IQuery<Pair<Long, Class<T>>, Optional<T>> clone() {
        return new ById();
    }
}
