package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;


@IDatabaseClass(tableName = "Methods")
public class JavaMethod extends JavaSpecificComponent{

    @IDatabaseField(columnName = "id",save = false,isID = true)
    public int id;


    @IDatabaseField(columnName = "information",fromJSON = true)
    public MethodInfoJSON json;

}
