package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;


@IDatabaseClass(tableName = "JavaClass",idName = "id_JC")
public class JavaClass extends JavaSourceObject {


    @IDatabaseField(columnName = "id_JC", save = false, isID = true)
    public int id;


    @IDatabaseField(columnName = "information", fromJSON = true)
    public MethodInfoJSON json;

}
