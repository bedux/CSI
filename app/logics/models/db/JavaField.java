package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;
import logics.models.db.information.FieldsInfoJSON;


@IDatabaseClass(tableName = "JavaField",idName = "id")
public class JavaField extends JavaSpecificComponent {

    @IDatabaseField(columnName = "id", save = false, isID = true)
    public long id;


    @IDatabaseField(columnName = "information", fromJSON = true)
    public FieldsInfoJSON json;

}
