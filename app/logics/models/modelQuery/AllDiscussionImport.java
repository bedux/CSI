package logics.models.modelQuery;

import logics.models.db.ImportTable;
import logics.models.db.JavaImport;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by bedux on 29/04/16.
 */
public class AllDiscussionImport implements IQuery<List<JavaImport>,List<ImportTable>> {
    @Override
    public List<ImportTable> execute(List<JavaImport> javaImport) {
        List<ImportTable> importDiscussions = new AllImportFromDiscussion().execute(null);
        return importDiscussions.parallelStream().filter(y -> {
            long s = javaImport.stream().filter(x -> {
                        String sToTest = y.packageDiscussion.replace(".*", "");
                        return x.getJson().name.contains(sToTest);
                    }
            ).count();
            return s > 0;
        }).collect(Collectors.toList());    }

    @Override
    public IQuery<List<JavaImport>, List<ImportTable>> clone() {
        return new AllDiscussionImport() ;
    }
}
