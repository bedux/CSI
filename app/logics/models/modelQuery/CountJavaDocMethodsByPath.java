package logics.models.modelQuery;

import logics.databaseCache.DatabaseModels;
import logics.models.db.JavaClass;
import logics.models.db.JavaDoc;
import logics.models.db.JavaFile;
import logics.models.db.JavaMethod;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by bedux on 29/04/16.
 */
public class CountJavaDocMethodsByPath implements IQuery<String,Long> {

    @Override
    public Long execute(String param) {
        List<JavaFile> jfs = DatabaseModels.getInstance().getAll(JavaFile.class).stream().filter(x->x.getPath().equals(param)).collect(Collectors.toList());
        final List<Long> l = jfs.stream().flatMap(x ->
                {
                    final List<JavaClass> javaClassList = x.getListOfJavaClass();
                    return javaClassList.stream().flatMap(y ->
                    {
                        final List<JavaMethod> javaMethods =   y.getListOfMethod();
                        return javaMethods.stream().map(z -> (Long) z.getId());
                    });
                }
        ).collect(Collectors.toList());

        return DatabaseModels.getInstance().getAll(JavaDoc.class).stream()
                .filter(x->l.stream().anyMatch(y -> x.getContainsTransverseInformation()==y)).count();
    }

    @Override
    public IQuery<String, Long> clone() {
        return new CountJavaDocMethodsByPath();
    }
}
