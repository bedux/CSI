package logics.models.modelQuery;

import logics.databaseCache.DatabaseModels;
import logics.models.db.JavaMethod;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by bedux on 29/04/16.
 */
public class AllJavaMethodOfRepositoryVersion implements IQuery<Long,List<String>> {
    @Override
    public List<String> execute(Long id) {
        return DatabaseModels.getInstance().getAll(JavaMethod.class).stream().filter(x -> x.getJavaClassConcrete().getJavaFileConcrete().getRepositoryVersionConcrete().getId() == id).map(x -> x.getJson().signature)
                .collect(Collectors.toList());

    }

    @Override
    public IQuery<Long, List<String>> clone() {
        return new AllJavaMethodOfRepositoryVersion();
    }
}
