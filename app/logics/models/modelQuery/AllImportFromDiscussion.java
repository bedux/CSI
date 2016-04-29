package logics.models.modelQuery;

import logics.databaseCache.DatabaseModels;
import logics.models.db.ImportTable;

import java.util.List;

/**
 * Created by bedux on 29/04/16.
 */
public class AllImportFromDiscussion  implements IQuery<Void,List<ImportTable>>{
    /***
     * return a list of ImportDiscussions
     *
     * @return a list of the import
     */
    @Override
    public List<ImportTable> execute(Void param) {
        return   DatabaseModels.getInstance().getAll(ImportTable.class);
    }

    @Override
    public IQuery<Void, List<ImportTable>> clone() {
        return new AllImportFromDiscussion();
    }
}
