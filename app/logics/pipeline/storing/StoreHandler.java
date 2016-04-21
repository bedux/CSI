package logics.pipeline.storing;

import exception.CustomException;
import interfaces.Handler;
import logics.Definitions;
import logics.databaseUtilities.SaveClassAsTable;
import logics.models.db.JavaFile;
import logics.models.db.information.JavaFileInformation;
import logics.models.db.RepositoryVersion;
import logics.models.db.TextFile;
import play.Logger;

import java.nio.file.FileSystems;
import java.nio.file.Files;

/**
 * Created by bedux on 07/03/16.
 */
public class StoreHandler implements Handler<StoreHandlerParam, StoreHandlerResult> {

    /**
     * This Handler has the goal to store all the file in the repository in the database.
     * @param param The StoreHandlerParam parameter
     * @return StoreHandlerResult
     */

    @Override
    public StoreHandlerResult process(StoreHandlerParam param) {
        String path = Definitions.repositoryPath;

        path += param.repositoryVersion.id;
        try {
            Files.walk(FileSystems.getDefault().getPath(path)).forEach((x) -> {
                String s = clearPath(x.normalize().toString(), param.repositoryVersion);
                String fn = s.substring(s.lastIndexOf(".") + 1);

                if (Files.isRegularFile(x) && fn.indexOf("java") == 0) {

                    JavaFile javaFile = new JavaFile();
                    javaFile.path = s;
                    javaFile.name = x.getFileName().toString();
                    javaFile.json = new JavaFileInformation();
                    javaFile.repositoryVersionId = param.repositoryVersion.id;
                    try {
                        int id = new SaveClassAsTable().save(javaFile);
                        Logger.info("Saved as Java File " + id + " " + s);
                    } catch (Exception e) {

                        throw new CustomException(e);
                    }

                } else if (Files.isRegularFile(x)) {

                    TextFile textFile = new TextFile();
                    textFile.path = s;
                    textFile.name = x.getFileName().toString();
                    textFile.repositoryVersionId = param.repositoryVersion.id;
                    textFile.json = new JavaFileInformation();

                    try {

                        int id = new SaveClassAsTable().save(textFile);
                        Logger.info("Saved as Regular file" + id + " " + s);
                    } catch (Exception e) {
                        throw new CustomException(e);
                    }

                }
            });
        } catch (Exception e1) {
            throw new CustomException(e1);
        }
        return new StoreHandlerResult(param.repositoryVersion);
    }


    /**
     *  given a path return a string that has the correct root
     * @param s the Path
     * @param repository    Repository id
     * @return
     */
    private String clearPath(String s, RepositoryVersion repository) {
        return s.substring(s.indexOf("repoDownload/" + repository.id) + ("repoDownload/").length());

    }
}
