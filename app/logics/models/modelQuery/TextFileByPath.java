package logics.models.modelQuery;

import logics.databaseCache.DatabaseModels;
import logics.models.db.TextFile;

import java.util.Optional;

/**
 * Created by bedux on 29/04/16.
 */
public class TextFileByPath implements IQuery<String,Optional<TextFile>> {
    @Override
    public Optional<TextFile> execute(String param) {
        return DatabaseModels.getInstance().getAll(TextFile.class).stream().filter(x->x.getPath().equals(param)).findFirst();
    }

    @Override
    public IQuery<String, Optional<TextFile>> clone() {
        return new TextFileByPath();
    }
}
