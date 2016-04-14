package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;
import logics.models.db.information.MethodInfoJSON;

/**
 * Created by bedux on 11/04/16.
 */
@IDatabaseClass(tableName = "discussion")
public class StackOFDiscussion {
    @IDatabaseField(columnName = "id", save = false, isID = true)
    public long id;


    @IDatabaseField(columnName = "url")
    public String packageDiscussion;
}


