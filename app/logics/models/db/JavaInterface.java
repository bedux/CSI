package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;


@IDatabaseClass(tableName = "JavaInterface",idName = "id")
public class JavaInterface extends JavaSourceObject {


    @IDatabaseField(columnName = "id", save = false, isID = true)
    public long id;


    @IDatabaseField(columnName = "information", fromJSON = true)
    public MethodInfoJSON json;

}
