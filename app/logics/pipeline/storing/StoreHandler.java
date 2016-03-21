package logics.pipeline.storing;

import exception.CustomException;
import interfaces.Handler;
import logics.Definitions;
import logics.models.db.ComponentInfo;
import logics.models.db.RepositoryVersion;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;

/**
 * Created by bedux on 07/03/16.
 */
public class StoreHandler implements Handler<StoreHandlerParam,StoreHandlerResult> {

    @Override
    public StoreHandlerResult process(StoreHandlerParam param) {
        String path = Definitions.repositoryPath;

        path+=param.repositoryVersion.id;
        try {
            Files.walk(FileSystems.getDefault().getPath(path)).forEach((x) -> {

                if (Files.isRegularFile(x)) {
                    String s = clearPath(x.normalize().toString(),param.repositoryVersion);
                    ComponentInfo c = ComponentInfo.createComponentInfo(param.repositoryVersion, s);
                    param.repositoryVersion.addComponentInfo(c);
                    param.nOfFile++;
                }
            });
        } catch (IOException e1) {

            throw new CustomException(e1);

        }finally {
            param.repositoryVersion.setnumOfFile(param.nOfFile);
            param.repositoryVersion.update();
        }
        return new StoreHandlerResult(param.repositoryVersion);
    }


    private String clearPath(String s,RepositoryVersion repository) {
        return s.substring(s.indexOf("repoDownload/" + repository.id) + ("repoDownload/").length());

    }
}
