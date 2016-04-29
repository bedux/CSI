package logics.models.modelQuery;

import logics.databaseCache.DatabaseModels;
import logics.models.db.JavaInterface;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by bedux on 29/04/16.
 */
public class AllJavaInterfaceByJavaFile implements IQuery<Long,List<JavaInterface>> {
    @Override
    public List<JavaInterface> execute(Long id) {
        return DatabaseModels.getInstance().getAll(JavaInterface.class).stream().filter(x->x.getJavaFileConcrete().getId() ==id).collect(Collectors.toList());
    }

    @Override
    public IQuery<Long, List<JavaInterface>> clone() {
        return new AllJavaInterfaceByJavaFile();
    }
}
