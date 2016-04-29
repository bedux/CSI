package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;
import logics.databaseUtilities.ManyToOne;

/**
 * Created by bedux on 29/04/16.
 */
@IDatabaseClass(tableName = "import_discussion")

public class ImportDiscussion {

    @IDatabaseField(columnName = "idd", save = false, isID = true)
    private long idd;

    @IDatabaseField(columnName = "idi", save = false, isID = true)
    private long idi;


    private StackOFDiscussion discussion;
    @ManyToOne(columnName = "discussion",columnNameRefTable = "idd")
    public StackOFDiscussion getDiscussionConcrete() {
        return discussion;
    }


    private ImportTable importT;
    @ManyToOne(columnName = "importT",columnNameRefTable = "idi")
    public ImportTable getImportConcrete() {
        return importT;
    }


}

