package logics.models.modelQuery;

import exception.SQLnoResult;
import logics.databaseUtilities.Pair;
import logics.models.db.JavaFile;
import logics.models.db.JavaImport;
import logics.models.db.MethodTable;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by bedux on 29/04/16.
 */
public class CountingExternalPackagePercentage implements IQuery<String,Long>{


    @Override
    public Long execute(String path) {
        JavaFile jf =new JavaFileByPath().execute(path).orElseThrow(() -> new SQLnoResult());
        final List<JavaImport> nonLocalImport =  new AllNonLocalImport().execute(new Pair(jf.getId(),jf.getRepositoryVersionConcrete().getId()));
        final List<JavaImport> importDiscussions = new AllDiscussedJavaImport().execute(nonLocalImport);

        final List<String> allJavaMethods  = new AllJavaMethodOfRepositoryVersion().execute(jf.getRepositoryVersionConcrete().getId());
        final List<String> currentMethods =  new AllJavaMethodCallFormPath().execute(path).stream().collect(Collectors.toList());
        final List<String> javaMethods =currentMethods.stream().distinct().filter(x -> !allJavaMethods.stream().anyMatch(y -> y.equals(x))).collect(Collectors.toList());
        final List<MethodTable> discussedMethod = new AllMethodDiscussed().execute(javaMethods);






        float percJavaMethodCoverage;
        if(javaMethods.size()>0) {
            percJavaMethodCoverage =  ((float) discussedMethod.size());
        }else{
            percJavaMethodCoverage = 0;
        }

        float percJavaImportCoverage;
        if(nonLocalImport.size()>0){
            percJavaImportCoverage  =  ((float)(importDiscussions.size()));
        }else{
            percJavaImportCoverage = 0;
        }

        return  (long) ((percJavaMethodCoverage+percJavaImportCoverage)/2.0);
    }

    @Override
    public IQuery<String, Long> clone() {
        return new CountingExternalPackagePercentage();
    }
}
