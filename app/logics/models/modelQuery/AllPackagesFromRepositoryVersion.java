package logics.models.modelQuery;

import logics.databaseCache.DatabaseModels;
import logics.models.db.JavaPackage;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by bedux on 29/04/16.
 */
public class AllPackagesFromRepositoryVersion implements IQuery<Long,List<JavaPackage>> {
    @Override
    public List<JavaPackage> execute(Long param) {
        final List<JavaPackage> importDiscussion = DatabaseModels.getInstance().getAll(JavaPackage.class).stream().filter(x->x.getJavaFileConcrete().getRepositoryVersionConcrete().getId() == param).collect(Collectors.toList());
        return importDiscussion;
    }

    @Override
    public IQuery<Long, List<JavaPackage>> clone() {
        return new AllPackagesFromRepositoryVersion();
    }
}
