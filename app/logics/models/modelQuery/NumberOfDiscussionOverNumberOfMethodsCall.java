package logics.models.modelQuery;

import exception.SQLnoResult;
import logics.databaseUtilities.Pair;
import logics.models.db.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by bedux on 29/04/16.
 */
public class NumberOfDiscussionOverNumberOfMethodsCall implements IQuery<String,Long>{


    @Override
    public Long execute(String path) {


            JavaFile jf =new JavaFileByPath().execute(path).orElseThrow(() -> new SQLnoResult());
            final List<String> allJavaMethods  = new AllJavaMethodOfRepositoryVersion().execute(jf.getRepositoryVersionConcrete().getId()).stream().distinct().collect(Collectors.toList());
            final List<String> pathMethodCall = new AllJavaMethodCallFormPath().execute(path).stream().distinct().collect(Collectors.toList());
            final List<String> realMethod =   pathMethodCall.stream().filter(x -> allJavaMethods.stream().filter(z -> z.equals(x)).count() == 0).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
            final long numberOfmethodDiscussed =  new AllMethodDiscussed().execute(realMethod).size();
            System.out.println(realMethod+" "+path+" "+numberOfmethodDiscussed);





        final List<JavaImport> nonLocalImport =  new AllNonLocalImport().execute(new Pair(jf.getId(),jf.getRepositoryVersionConcrete().getId()));
        final List<JavaImport> importDiscussions = new AllDiscussedJavaImport().execute(nonLocalImport);
//
//        final List<String> allJavaMethods  = new AllJavaMethodOfRepositoryVersion().execute(jf.getRepositoryVersionConcrete().getId());
//        final List<String> currentMethods =  new AllJavaMethodCallFormPath().execute(path).stream().collect(Collectors.toList());
//        final List<String> javaMethods =currentMethods.stream().distinct().filter(x -> !allJavaMethods.stream().anyMatch(y -> y.equals(x))).collect(Collectors.toList());
//        final List<MethodTable> discussedMethod = new AllMethodDiscussed().execute(javaMethods);
//
//        System.out.println(ja vaMethods.size()+" "+discussedMethod.size());
//
//        float percJavaMethodCoverage;
//        if(realMethod.size()>0) {
//            percJavaMethodCoverage =  (((float) numberOfmethodDiscussed)/((float) realMethod.size()))*100;
//        }else{
//            percJavaMethodCoverage = 0;
//        }
//        System.out.println(percJavaMethodCoverage);
        return numberOfmethodDiscussed + importDiscussions.size();
//
//        float percJavaImportCoverage;
//        if(nonLocalImport.size()>0){
//            percJavaImportCoverage  =  (((float)(importDiscussions.size()))/((float) nonLocalImport.size()))*100f;
//        }else{
//            percJavaImportCoverage = 100;
//        }
//
//        return  (long) ((percJavaMethodCoverage+percJavaImportCoverage));
    }

    @Override
    public IQuery<String, Long> clone() {
        return new NumberOfDiscussionOverNumberOfMethodsCall();
    }
}
