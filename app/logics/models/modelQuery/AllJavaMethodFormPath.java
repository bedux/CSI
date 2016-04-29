package logics.models.modelQuery;

import logics.databaseCache.DatabaseModels;
import logics.models.db.JavaClass;
import logics.models.db.JavaInterface;
import logics.models.db.JavaMethod;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by bedux on 29/04/16.
 */
public class AllJavaMethodFormPath implements IQuery<String,List<String>> {
    @Override
    public List<String> execute(String path) {
        List<String> res = DatabaseModels.getInstance().getAll(JavaMethod.class).stream().filter(x -> x.getJavaClassConcrete().getJavaFileConcrete().getPath().equals(path)).filter(x -> x.getJson() != null).flatMap(x -> x.getJson().variableDeclaration.stream())
                .collect(Collectors.toList());
        res.addAll(
                DatabaseModels.getInstance().getAll(JavaClass.class).stream().filter(x -> x.getJavaFileConcrete().getPath().equals(path)).filter(x->x.getJson() != null).flatMap(x ->
                        x.getJson().variableDeclaration.stream())
                        .collect(Collectors.toList()));

        res.addAll(DatabaseModels.getInstance().getAll(JavaInterface.class).stream().filter(x -> x.getJavaFileConcrete().getPath().equals(path)).filter(x->x.getJson() != null).flatMap(x -> x.getJson().variableDeclaration.stream())
                .collect(Collectors.toList()));
        return res;
    }

    @Override
    public IQuery<String, List<String>> clone() {
        return new AllJavaMethodFormPath();
    }
}
