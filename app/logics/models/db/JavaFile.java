package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;


@IDatabaseClass(tableName = "JavaFile",idName = "id_JF")
public class JavaFile extends File {


    @IDatabaseField(columnName = "id_JF", save = false, isID = true)
    public int id;


    @IDatabaseField(columnName = "information", fromJSON = true)
    public JavaFileInformation json;


}
