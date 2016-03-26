package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;


@IDatabaseClass(tableName = "BinaryFile")
public class BinaryFile extends File{


    @IDatabaseField(columnName = "id",save = false,isID = true)
    public int id;


    @IDatabaseField(columnName = "information",fromJSON = true)
    public JavaFileInformation json;

}
