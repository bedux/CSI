package logics.models.query;

import exception.CustomException;
import exception.SQLnoResult;
import logics.models.db.JavaFile;
import logics.models.db.JavaImport;
import logics.models.db.MethodDiscussed;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;


public class CountingExternalPackagePercentage implements  IComputeAttributeContainer {
    @Override
    public  long executeAndGetResult(String path) {
        try {
            JavaFile jf = QueryList.getInstance().getJavaFileByPath(path).orElseThrow(()->new SQLnoResult());
            final List<JavaImport> nonLocalImport =  QueryList.getInstance().getAllNonLocalImport(jf.id,jf.repositoryVersionId);
            final List<JavaImport> importDiscussions =  QueryList.getInstance().getAllDiscussedJavaImport(nonLocalImport);

            final List<String> allJavaMethods  = QueryList.getInstance().getAllJavaMethodOfRepositoryVersion(jf.repositoryVersionId);
            final List<String> javaMethods = QueryList.getInstance().getAllJavaMethodFormPath(path).stream().distinct().filter(x -> !allJavaMethods.stream().anyMatch(y -> y.equals(x))).collect(Collectors.toList());
            final List<MethodDiscussed> discussedMethod = QueryList.getInstance().getAllMethodDiscussed(javaMethods);



            float percJavaMethodCoverage;
            if(javaMethods.size()>0) {
                percJavaMethodCoverage =  (((float) discussedMethod.size())/((float) javaMethods.size()))*100f;
            }else{
                percJavaMethodCoverage = 100;
            }

            float percJavaImportCoverage;
            if(nonLocalImport.size()>0){
                percJavaImportCoverage  =  (((float)(importDiscussions.size()))/((float) nonLocalImport.size()))*100f;
            }else{
                percJavaImportCoverage = 100;
            }

            return  (long) ((percJavaMethodCoverage+percJavaImportCoverage)/2.0f);


        } catch (SQLException e){
            throw new CustomException(e);
        } catch (InstantiationException e) {
            throw new CustomException(e);
        } catch (IllegalAccessException e) {
            throw new CustomException(e);
        }
    }

    @Override
    public long executeAndGetResult(long id) {
        throw new CustomException("Not Implememtded CountingExternalPacking");
    }
    @Override
    public CountingExternalPackagePercentage clone() {
        return new CountingExternalPackagePercentage();
    }
}
