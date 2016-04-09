package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;
import logics.models.db.information.MethodInfoJSON;


@IDatabaseClass(tableName = "JavaMethod",idName = "id")
public class JavaMethod extends JavaSpecificComponent {

    @IDatabaseField(columnName = "id", save = false, isID = true)
    public long id;


    @IDatabaseField(columnName = "information", fromJSON = true)
    public MethodInfoJSON json;

}
