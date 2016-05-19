package logics.pipeline.storing;

import exception.CustomException;
import interfaces.Handler;
import logics.Definitions;
import logics.models.newDatabase.JavaFile;
import logics.models.newDatabase.RepositoryVersion;
import logics.models.newDatabase.TextFile;

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

//                    JavaFile javaFile = new JavaFile();


                    JavaFile javaFile = new JavaFile();
                    javaFile.name = s;
                 //   javaFile.setName(x.getFileName().toString()); //TODO
                    javaFile.repositoryVersion =  param.repositoryVersion;
                    param.repositoryVersion.javaFileList.add(javaFile);
                    javaFile.save();

                } else if (Files.isRegularFile(x)) {

                    TextFile textFile = new TextFile();
                    textFile.name = s;
                    //textFile.setName( x.getFileName().toString());//TODO
                    textFile.repositoryVersion = param.repositoryVersion;
                    param.repositoryVersion.textFileList.add(textFile);

                    textFile.save();


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
