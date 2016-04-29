package logics.models.modelQuery;

import logics.models.db.ImportTable;
import logics.models.db.StackOFDiscussion;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by bedux on 29/04/16.
 */
public class SOFDiscussionFromImportDiscussion implements IQuery<List<ImportTable>,List<StackOFDiscussion>> {
    @Override
    public List<StackOFDiscussion> execute(List<ImportTable> param) {
        return param.stream().flatMap(x->x.getListOfImportDiscussion().stream()).map(x->x.getDiscussionConcrete()).collect(Collectors.toList());
    }

    @Override
    public IQuery<List<ImportTable>, List<StackOFDiscussion>> clone() {
        return new SOFDiscussionFromImportDiscussion();
    }
}
