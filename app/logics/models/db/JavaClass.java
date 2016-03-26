package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;


@IDatabaseClass(tableName = "JavaClass")
public class JavaClass extends JavaSourceObject{


    @IDatabaseField(columnName = "id",save = false,isID = true)
    public int id;


    @IDatabaseField(columnName = "information",fromJSON = true)
    public MethodInfoJSON json;

}
