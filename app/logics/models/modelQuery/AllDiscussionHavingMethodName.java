package logics.models.modelQuery;

import logics.databaseCache.DatabaseModels;
import logics.models.db.MethodDiscussion;
import logics.models.db.StackOFDiscussion;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by bedux on 29/04/16.
 */
public class AllDiscussionHavingMethodName implements IQuery<List<String>,List<StackOFDiscussion>> {
    @Override
    public List<StackOFDiscussion> execute(List<String> param) {
        return DatabaseModels.getInstance().getAll(MethodDiscussion.class)
                .stream()
                .filter(x->param.stream().anyMatch(y -> y.contentEquals(x.getMethodConcrete().getMethodName())))
                .map(x->x.getDiscussionConcrete())
                .collect(Collectors.toList());
    }

    @Override
    public IQuery<List<String>, List<StackOFDiscussion>> clone() {
        return new AllDiscussionHavingMethodName();
    }
}
