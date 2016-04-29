package logics.models.query;

import exception.CustomException;
import exception.SQLnoResult;
import logics.models.db.*;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;


public class CountingExternalPackage implements  IComputeAttributeContainer {
    @Override
    public  long executeAndGetResult(String path) {
        try {

            JavaFile jf = QueryList.getInstance().getJavaFileByPath(path).orElseThrow(()->new SQLnoResult());

            final List<JavaImport> nonLocalImport =  QueryList.getInstance().getAllNonLocalImport(jf.getId(),jf.getRepositoryVersionConcrete().getId());
            final List<JavaImport> importDiscussions =  QueryList.getInstance().getAllDiscussedJavaImport(nonLocalImport);

            final List<String> allJavaMethods  = QueryList.getInstance().getAllJavaMethodOfRepositoryVersion(jf.getRepositoryVersionConcrete().getId());
            final List<String> javaMethods = QueryList.getInstance().getAllJavaMethodFormPath(path).stream().distinct().filter(x -> !allJavaMethods.stream().anyMatch(y -> y.equals(x))).collect(Collectors.toList());
            final List<MethodTable> discussedMethod = QueryList.getInstance().getAllMethodDiscussed(javaMethods);



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


        } catch (SQLException e){
            throw new CustomException(e);
        }
    }

    @Override
    public long executeAndGetResult(long id) {
        throw new CustomException("Not Implememtded CountingExternalPacking");
    }
    @Override
    public CountingExternalPackage clone() {
        return new CountingExternalPackage();
    }
}
