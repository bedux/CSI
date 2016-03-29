package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;


@IDatabaseClass(tableName = "JavaField",idName = "id")
public class JavaField extends JavaSpecificComponent {

    @IDatabaseField(columnName = "id", save = false, isID = true)
    public int id;


    @IDatabaseField(columnName = "information", fromJSON = true)
    public FieldsInfoJSON json;

}
