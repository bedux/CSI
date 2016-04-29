package logics.models.modelQuery;

import logics.databaseCache.DatabaseModels;
import logics.models.db.JavaClass;
import logics.models.db.JavaInterface;
import logics.models.db.JavaMethod;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by bedux on 29/04/16.
 */
public class AllJavaMethodFormPath implements IQuery<String,List<JavaMethod>> {
    @Override
    public List<JavaMethod> execute(String path) {
        return DatabaseModels.getInstance().getAll(JavaMethod.class).stream().filter(x -> x.getJavaClassConcrete().getJavaFileConcrete().getPath().equals(path)).collect(Collectors.toList());

    }

    @Override
    public IQuery<String, List<JavaMethod>> clone() {
        return new AllJavaMethodFormPath();
    }
}
