package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;


@IDatabaseClass(tableName = "JavaSourceObject")
public class JavaSourceObject{

    @IDatabaseField(columnName = "id",save = false,isID = true)
    public int id;

    @IDatabaseField(columnName = "JavaFile")
    public int javaFile;


}
