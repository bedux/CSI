package logics.models.modelQuery;

import logics.databaseCache.DatabaseModels;
import logics.models.db.JavaClass;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by bedux on 29/04/16.
 */
public class AllJavaClassByJavaFile implements IQuery<Long,List<JavaClass>> {
    @Override
    public List<JavaClass> execute(Long id) {
        return DatabaseModels.getInstance().getAll(JavaClass.class).stream().
                filter(x->x.getJavaFileConcrete().getId() == id).collect(Collectors.toList());
    }

    @Override
    public IQuery<Long, List<JavaClass>> clone() {
        return new AllJavaClassByJavaFile();
    }
}
