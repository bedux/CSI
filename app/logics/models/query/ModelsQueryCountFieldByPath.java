package logics.models.query;

import logics.databaseCache.DatabaseModels;
import logics.models.db.JavaField;
import logics.models.db.JavaMethod;

/**
 * Created by bedux on 28/04/16.
 */
public class ModelsQueryCountFieldByPath implements IComputeAttributeContainer{

    @Override
    public long executeAndGetResult(String path) {
        return DatabaseModels.getInstance().getAll(JavaField.class).stream().filter(x->x.getJavaClassConcrete().getJavaFileConcrete().getPath().equals(path)).count();

    }

    @Override
    public long executeAndGetResult(long id) {
        return 0;
    }

    @Override
    public IComputeAttributeContainer clone() {
        return  new ModelsQueryCountFieldByPath();
    }
}
