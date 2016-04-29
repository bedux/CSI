package logics.models.modelQuery;

import logics.databaseCache.DatabaseModels;
import logics.models.db.JavaFile;

import java.util.Optional;

/**
 * Created by bedux on 29/04/16.
 */
public class JavaFileByPath implements IQuery<String,Optional<JavaFile>> {
    @Override
    public Optional<JavaFile> execute(String path) {
        return DatabaseModels.getInstance().getAll(JavaFile.class).stream().filter(x->x.getPath().equals(path)).findFirst();
    }

    @Override
    public IQuery<String, Optional<JavaFile>> clone() {
        return new JavaFileByPath();
    }
}
