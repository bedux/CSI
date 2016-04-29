package logics.models.modelQuery;

import exception.SQLnoResult;
import logics.databaseUtilities.Pair;
import logics.models.db.JavaFile;
import logics.models.db.JavaImport;
import logics.models.db.MethodTable;
import logics.models.query.QueryList;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by bedux on 29/04/16.
 */
public class QueryComputeJavaDoc implements IQuery<String,Long> {
    @Override
    public Long execute(String path) {
        JavaFile jf =new JavaFileByPath().execute(path).orElseThrow(() -> new SQLnoResult());

        final List<JavaImport> nonLocalImport =  new AllNonLocalImport().execute(new Pair<Long,Long>((Long)jf.getId(),(Long)jf.getRepositoryVersionConcrete().getId()));
        final List<JavaImport> importDiscussions =  new AllDiscussedJavaImport().execute(nonLocalImport);

        final List<String> allJavaMethods  =new AllJavaMethodOfRepositoryVersion().execute(jf.getRepositoryVersionConcrete().getId());
        final List<String> javaMethods = new AllJavaMethodCallFormPath().execute(path).stream().distinct().filter(x -> !allJavaMethods.stream().anyMatch(y -> y.equals(x))).collect(Collectors.toList());
        final List<MethodTable> discussedMethod;
            discussedMethod =new AllMethodDiscussed().execute(javaMethods);


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
//            if (!(percJavaImportCoverage<=100 && percJavaMethodCoverage <=100)){
//                System.out.println("**************************************");
//
//                System.out.println(jf.path);
//                System.out.println(allJavaMethods.size());
//                System.out.println(javaMethods.size());
//                System.out.println(discussedMethod.size());
//
//
//
//                System.out.println();
//                javaMethods.stream().map(x -> x + " | ").forEach(System.out::print);
//                System.out.println();
//
//                discussedMethod.stream().map(x->x.methodName+" | ").forEach(System.out::print);
//                System.out.println();
//
//            }

//
        return  (long) ((percJavaMethodCoverage+percJavaImportCoverage));



}

    @Override
    public IQuery<String, Long> clone() {
        return new QueryComputeJavaDoc();
    }
}
