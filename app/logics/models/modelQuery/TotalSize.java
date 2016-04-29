package logics.models.modelQuery;

import exception.CustomException;
import logics.Definitions;

import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by bedux on 29/04/16.
 */
public class TotalSize implements IQuery<Long,Long> {

    @Override
    public Long execute(Long param) {
        try {
            return Files.walk(new java.io.File(Definitions.repositoryPath + param).toPath())
                    .filter(path -> !Files.isDirectory(path)).mapToLong(x -> x.toFile().length()).sum();
        } catch (IOException e) {
            throw new CustomException(e);
        }
    }

    @Override
    public IQuery<Long, Long> clone() {
        return new TotalSize();
    }
}
