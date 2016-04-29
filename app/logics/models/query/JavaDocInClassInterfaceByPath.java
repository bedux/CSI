package logics.models.query;

import java.util.HashMap;
import java.util.List;

/**
 * Created by bedux on 27/04/16.
 */
public class JavaDocInClassInterfaceByPath extends QueryWithPath {
    public JavaDocInClassInterfaceByPath(String query, HashMap<Integer, Object> params, int pathId) {
        super(query, params, pathId);
    }

    public JavaDocInClassInterfaceByPath(String query, int pathId) {
        super(query, pathId);
    }

    public JavaDocInClassInterfaceByPath(String query, List<Integer> pathId) {
        super(query, pathId);
    }



    /***("select COUNT(*) from JavaDoc where JavaDoc.ContainsTransverseInformation in  (select id from JavaMethod where JavaMethod.JavaSource  in (select JavaSourceObject.id from JavaSourceObject,JavaFile where JavaSourceObject.javaFile = JavaFile.id AND JavaFile.path = ?));"
     * @return return a list of The type of the query table.
     */
    @Override
    public List<CountQuery> execute() {
        return super.execute();
    }

    /***
     * @return the first element of the list of result.
     */
    @Override
    public CountQuery executeAndGetFirst() {
        return super.executeAndGetFirst();
    }
}
