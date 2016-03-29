package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;


@IDatabaseClass(tableName = "JavaMethod",idName = "id_M")
public class JavaMethod extends JavaSpecificComponent {

    @IDatabaseField(columnName = "id_M", save = false, isID = true)
    public int id;


    @IDatabaseField(columnName = "information", fromJSON = true)
    public MethodInfoJSON json;

}
