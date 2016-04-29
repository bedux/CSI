package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;
import logics.databaseUtilities.ManyToOne;

/**
 * Created by bedux on 29/04/16.
 */
@IDatabaseClass(tableName = "method_discussion")

public class MethodDiscussion {

    @IDatabaseField(columnName = "idd", save = false, isID = true)
    public long idd;

    @IDatabaseField(columnName = "idm", save = false, isID = true)
    public long idi;


    private StackOFDiscussion discussion;
    @ManyToOne(columnName = "discussion",columnNameRefTable = "idd")
    public StackOFDiscussion getDiscussionConcrete() {
        return discussion;
    }


    private MethodTable methodT;
    @ManyToOne(columnName = "methodT",columnNameRefTable = "idm")
    public MethodTable getMethodConcrete() {
        return methodT;
    }


}

