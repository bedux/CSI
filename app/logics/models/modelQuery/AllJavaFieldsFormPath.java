package logics.models.modelQuery;

import logics.databaseCache.DatabaseModels;
import logics.models.db.JavaClass;
import logics.models.db.JavaField;
import logics.models.db.JavaInterface;
import logics.models.db.JavaMethod;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by bedux on 29/04/16.
 */
public class AllJavaFieldsFormPath implements IQuery<String,List<JavaField>> {
    @Override
    public List<JavaField> execute(String path) {
        return DatabaseModels.getInstance().getAll(JavaField.class).stream().filter(x->x.getJavaClassConcrete().getJavaFileConcrete().getPath().equals(path)).collect(Collectors.toList());

    }

    @Override
    public IQuery<String, List<JavaField>> clone() {
        return new AllJavaFieldsFormPath();
    }
}
