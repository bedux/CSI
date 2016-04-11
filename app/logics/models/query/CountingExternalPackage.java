package logics.models.query;

import exception.CustomException;
import logics.analyzer.*;
import logics.models.db.ImportDiscussion;
import logics.models.db.JavaFile;
import logics.models.db.JavaImport;
import logics.models.db.JavaPackage;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by bedux on 11/04/16.
 */
public class CountingExternalPackage implements  IComputeAttributeContainer {
    @Override
    public long executeAndGetResult(String path) {
        try {
            JavaFile jf = QueryList.getInstance().getJavaFileByPath(path);
            List<JavaImport> nonLocalImport =  QueryList.getInstance().getAllNonLocalImport(jf.id,jf.repositoryVersionId);
            List<JavaImport> importDsicussions =  QueryList.getInstance().getAllDiscussedImport(nonLocalImport);
            if(nonLocalImport.size()==0) return 100;
            return  (long) (((float)(importDsicussions.size()) / (float)(nonLocalImport.size()))*100f);


        } catch (Exception e) {
            throw new CustomException(e);
        }
    }

    @Override
    public long executeAndGetResult(long id) {
        throw new CustomException("Not Implememtded CountingExternalPacking");
    }
}
