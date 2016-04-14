package logics.models.db;


import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;
import logics.models.db.information.MethodInfoJSON;

/**
 * Created by bedux on 14/04/16.
 */
@IDatabaseClass(tableName = "method")
public class MethodDiscussed {

    @IDatabaseField(columnName = "id", save = false, isID = true)
    public long id;


    @IDatabaseField(columnName = "methodname")
    public String packageDiscussion;

    @IDatabaseField(columnName = "params")
    public int nOfParam;
}

