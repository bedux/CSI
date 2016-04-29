package logics.models.modelQuery;

import logics.databaseCache.DatabaseModels;
import logics.models.db.JavaImport;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by bedux on 29/04/16.
 */
public class AllJavaImportFromJavaFile implements IQuery<Long,List<JavaImport>> {

    /***
     * Given a javaFile. return a list of his JavaImport
     *
     * @param param id of the file
     * @return a list of the import
     */
    @Override
    public List<JavaImport> execute(Long param) {
        List<JavaImport> importDiscussion  = DatabaseModels.getInstance().getAll(JavaImport.class).stream().filter(x->x.getJavaFileConcrete().getId() == param).collect(Collectors.toList());
        return importDiscussion;
    }

    @Override
    public IQuery<Long, List<JavaImport>> clone() {
        return new AllJavaImportFromJavaFile();
    }
}
