package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;


@IDatabaseClass(tableName = "JavaImport",idName = "id")
public class JavaImport extends JavaSourceObject {


    @IDatabaseField(columnName = "id", save = false, isID = true)
    public long id;


    @IDatabaseField(columnName = "information", fromJSON = true)
    public JavaFileInformation json;

}
