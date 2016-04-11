package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;
import logics.models.db.information.MethodInfoJSON;

/**
 * Created by bedux on 10/04/16.
 */
@IDatabaseClass(tableName = "import")
public class ImportDiscussion {
    @IDatabaseField(columnName = "id", save = false, isID = true)
    public long id;


    @IDatabaseField(columnName = "package")
    public String packageDiscussion;
}
