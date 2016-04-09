package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;
import logics.models.db.information.JavaDocIfoJSON;
import logics.models.db.information.TransverseInformation;

/**
 * Created by bedux on 25/03/16.
 */
@IDatabaseClass(tableName = "JavaDoc",idName = "id")
public class JavaDoc extends TransverseInformation {

    @IDatabaseField(columnName = "id", save = false, isID = true)
    public long id;

    @IDatabaseField(columnName = "information", fromJSON = true)
    public JavaDocIfoJSON json;

}
