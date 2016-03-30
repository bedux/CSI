package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;


@IDatabaseClass(tableName = "JavaSourceObject",idName = "id_JSO")
public class JavaSourceObject {

    @IDatabaseField(columnName = "id_JSO", save = false, isID = true)
    public long id;

    @IDatabaseField(columnName = "JavaFile")
    public long javaFile;


}
