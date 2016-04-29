package logics.models.modelQuery;

import javafx.util.Pair;
import logics.models.db.JavaImport;
import logics.models.db.JavaPackage;
import logics.models.query.QueryList;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by bedux on 29/04/16.
 */


public class AllNonLocalImport implements IQuery<Pair<Long, Long>, List<JavaImport>> {
    /***
     *
     * @param param Pair consist of id of the JavaFile and the id of the repositoryVersion
     * @return the list off all import not in the package
     */
    @Override
    public List<JavaImport> execute(Pair<Long, Long> param) {
        List<JavaImport> javaImports =new AllJavaImportFromJavaFile().execute(param.getKey());
        List<JavaPackage> packages = new AllPackagesFromRepositoryVersion().execute(param.getValue());
        return javaImports.parallelStream().
                filter(x ->
                        {
                            Boolean bb = !packages.parallelStream().anyMatch(y -> {
                                Boolean result = x.getJson().name.contains(y.getJson().name);
                                return result;
                            });
                            return bb;
                        }
                ).collect(Collectors.toList());
    }

    @Override
    public IQuery<Pair<Long, Long>, List<JavaImport>> clone() {
        return new AllNonLocalImport();
    }
}
