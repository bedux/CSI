package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;
import logics.models.db.information.JavaEnumInformation;

/**
 * Created by bedux on 31/03/16.
 */
@IDatabaseClass(tableName = "JavaEnum")
public class JavaEnum extends JavaSourceObject  {

    @IDatabaseField(columnName = "id", save = false, isID = true)
    public long id;


    @IDatabaseField(columnName = "information", fromJSON = true)
    public JavaEnumInformation json;


}
