package logics.models.query;

import exception.CustomException;
import logics.models.db.*;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;


public class CountingExternalPackage implements  IComputeAttributeContainer {
    @Override
    public long executeAndGetResult(String path) {
        try {
            JavaFile jf = QueryList.getInstance().getJavaFileByPath(path);
            final List<JavaImport> nonLocalImport =  QueryList.getInstance().getAllNonLocalImport(jf.id,jf.repositoryVersionId);
            final List<JavaImport> importDsicussions =  QueryList.getInstance().getAllDiscussedJavaImport(nonLocalImport);
            final List<StackOFDiscussion> discussionReleatedToImport = QueryList.getInstance().getGitDiscussionFromImportDiscussion(QueryList.getInstance().getAllDissussionImport(importDsicussions));

            final List<String> allJavaMethods  = QueryList.getInstance().getAllJavaMethodOfRepositoryVersion(jf.repositoryVersionId);
            final List<String> javaMethods = QueryList.getInstance().getAllJavaMethodFormPath(path).stream().distinct().filter(x -> !allJavaMethods.contains(x)).collect(Collectors.toList());
            final List<MethodDiscussed> discussedMethod = QueryList.getInstance().getAllMethodDiscussed(javaMethods);
            final List<StackOFDiscussion> discussionsReleatedToMethodName = QueryList.getInstance().getAllDiscussionHavingMethodName(javaMethods);



            float percJavaMethodCoverage;
            if(javaMethods.size()>0) {
                percJavaMethodCoverage =  ((float) discussedMethod.size()) / ((float) javaMethods.size()) * 100f;
            }else{
                percJavaMethodCoverage = 100;
            }

            float percJavaImportCoverage;
            if(nonLocalImport.size()>0){
                percJavaImportCoverage  =  (((float)(importDsicussions.size()) / (float)(nonLocalImport.size()))*100f);
            }else{
                percJavaImportCoverage = 100;
            }
            if (!(percJavaImportCoverage<=100 && percJavaMethodCoverage <=100)){
                System.out.println("**************************************");

                System.out.println(jf.path);
                System.out.println(allJavaMethods.size());
                System.out.println(javaMethods.size());
                System.out.println(discussedMethod.size());



                System.out.println();
                javaMethods.stream().map(x -> x + " | ").forEach(System.out::print);
                System.out.println();

                discussedMethod.stream().map(x->x.packageDiscussion+" | ").forEach(System.out::print);
                System.out.println();

            }

//
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
}
