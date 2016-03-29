package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;


@IDatabaseClass(tableName = "JavaImport",idName = "id_JI")
public class JavaImport extends JavaSourceObject {


    @IDatabaseField(columnName = "id_JI", save = false, isID = true)
    public int id;


    @IDatabaseField(columnName = "information", fromJSON = true)
    public JavaFileInformation json;

}
