package logics.models.modelQuery;

import logics.databaseCache.DatabaseModels;

import java.util.List;

/**
 * Created by bedux on 29/04/16.
 */
public class All<T> implements IQuery<Class<T>,List<T>> {

    @Override
    public List<T> execute(Class<T> param) {
        return DatabaseModels.getInstance().getAll(param);
    }

    @Override
    public IQuery<Class<T>, List<T>> clone() {
        return new All<T>();
    }
}
