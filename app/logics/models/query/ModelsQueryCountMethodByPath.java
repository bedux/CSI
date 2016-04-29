package logics.models.query;

import logics.databaseCache.DatabaseModels;
import logics.models.db.JavaMethod;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by bedux on 28/04/16.
 */
public class ModelsQueryCountMethodByPath implements IComputeAttributeContainer{

    @Override
    public synchronized long executeAndGetResult(final String path) {
        return Collections.synchronizedList(DatabaseModels.getInstance().getAll(JavaMethod.class)).stream().filter(x->x.getJavaClassConcrete().getJavaFileConcrete().getPath().equals(path)).count();

    }

    @Override
    public long executeAndGetResult(long id) {
        return 0;
    }

    @Override
    public IComputeAttributeContainer clone() {
        return  new ModelsQueryCountMethodByPath();
    }
}
