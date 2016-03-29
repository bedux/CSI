package logics.pipeline.storing;

import exception.CustomException;
import interfaces.Handler;
import logics.Definitions;
import logics.databaseUtilities.SaveClassAsTable;
import logics.models.db.JavaFile;
import logics.models.db.RepositoryVersion;
import logics.models.db.TextFile;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;

/**
 * Created by bedux on 07/03/16.
 */
public class StoreHandler implements Handler<StoreHandlerParam, StoreHandlerResult> {

    @Override
    public StoreHandlerResult process(StoreHandlerParam param) {
        String path = Definitions.repositoryPath;

        path += param.repositoryVersion.id;
        try {
            Files.walk(FileSystems.getDefault().getPath(path)).forEach((x) -> {
                String s = clearPath(x.normalize().toString(), param.repositoryVersion);
                String fn = s.substring(s.lastIndexOf(".") + 1);

                if (Files.isRegularFile(x) && fn.indexOf("java") == 0) {
                    System.out.print("JavaFile");
                    JavaFile javaFile = new JavaFile();
                    javaFile.path = s;
                    javaFile.name = x.getFileName().toString();
                    javaFile.repositoryVersionId = param.repositoryVersion.id;
                    try {
                        int id = new SaveClassAsTable().save(javaFile);
                        System.out.println("Saved as " + id);
                    } catch (Exception e) {
                        new CustomException(e);
                    }
//                    ComponentInfo c = ComponentInfo.createComponentInfo(param.repositoryVersion, s);
//                    param.repositoryVersion.addComponentInfo(c);
//                    param.nOfFile++;
                } else if (Files.isRegularFile(x)) {

                    TextFile textFile = new TextFile();
                    textFile.path = s;
                    textFile.name = x.getFileName().toString();
                    textFile.repositoryVersionId = param.repositoryVersion.id;
                    try {
                        int id = new SaveClassAsTable().save(textFile);
                        System.out.println("Saved as " + id);
                    } catch (Exception e) {
                        new CustomException(e);
                    }
                }
            });
        } catch (IOException e1) {

            throw new CustomException(e1);

        } finally {
//            param.repositoryVersion.setnumOfFile(param.nOfFile);
//            param.repositoryVersion.update();
        }
        return new StoreHandlerResult(param.repositoryVersion);
    }


    private String clearPath(String s, RepositoryVersion repository) {
        return s.substring(s.indexOf("repoDownload/" + repository.id) + ("repoDownload/").length());

    }
}
