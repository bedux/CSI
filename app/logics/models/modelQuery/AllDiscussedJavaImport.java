package logics.models.modelQuery;

import logics.models.db.ImportTable;
import logics.models.db.JavaImport;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by bedux on 29/04/16.
 */
public class AllDiscussedJavaImport implements IQuery<List<JavaImport>,List<JavaImport>> {
    @Override
    public List<JavaImport> execute(List<JavaImport> javaImport) {
        List<ImportTable> importDiscussion = new AllImportFromDiscussion().execute(null);
        return javaImport.parallelStream().filter(x -> {
            long s = importDiscussion.parallelStream().filter(y -> {
                        String sToTest = y.packageDiscussion.replace(".*", "");
                        return x.getJson().name.indexOf(sToTest) != -1;
                    }
            ).count();
            return s > 0;
        }).collect(Collectors.toList());    }

    @Override
    public IQuery<List<JavaImport>, List<JavaImport>> clone() {
        return new AllDiscussedJavaImport();
    }
}
