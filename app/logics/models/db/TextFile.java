package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;


@IDatabaseClass(tableName = "TextFile",idName = "id_TF")
public class TextFile extends File {

    @IDatabaseField(columnName = "id_TF", save = false, isID = true)
    public int id;


    @IDatabaseField(columnName = "information", fromJSON = true)
    public JavaFileInformation json;

}
