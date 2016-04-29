package logics.models.modelQuery;

import logics.databaseCache.DatabaseModels;
import logics.models.db.MethodDiscussion;
import logics.models.db.MethodTable;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by bedux on 29/04/16.
 */
public class AllMethodDiscussed implements IQuery<List<String>,List<MethodTable>> {
    @Override
    public List<MethodTable> execute(List<String> param) {
        return DatabaseModels.getInstance().getAll(MethodDiscussion.class)
                .stream()
                .filter(x->param.stream().filter(y -> y.equals(x.getMethodConcrete().getMethodName())).count()>0)
                .map(x->x.getMethodConcrete())
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public IQuery<List<String>, List<MethodTable>> clone() {
        return new AllMethodDiscussed();
    }
}
