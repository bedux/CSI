package logics.models.modelQuery;

import logics.databaseCache.DatabaseModels;
import logics.models.db.File;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by bedux on 29/04/16.
 */
public class FileByRepositoryVersion implements IQuery<Long,List<File>> {
    @Override
    public List<File> execute(Long param) {
        return  DatabaseModels.getInstance().getAll(File.class).stream().filter(x->x.getRepositoryVersionConcrete().getId() == param).collect(Collectors.toList());
    }

    @Override
    public IQuery<Long, List<File>> clone() {
        return new FileByRepositoryVersion();
    }
}
