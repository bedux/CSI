package logics.models.query;

import exception.CustomException;
import logics.models.db.JavaFile;
import logics.models.db.JavaImport;

import java.util.List;



public class CountingExternalPackage implements  IComputeAttributeContainer {
    @Override
    public long executeAndGetResult(String path) {
        try {
            JavaFile jf = QueryList.getInstance().getJavaFileByPath(path);
            List<JavaImport> nonLocalImport =  QueryList.getInstance().getAllNonLocalImport(jf.id,jf.repositoryVersionId);
            List<JavaImport> importDsicussions =  QueryList.getInstance().getAllDiscussedJavaImport(nonLocalImport);
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
