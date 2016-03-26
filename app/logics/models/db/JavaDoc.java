package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;

/**
 * Created by bedux on 25/03/16.
 */
@IDatabaseClass(tableName = "JavaDoc")
public class JavaDoc extends TrasversalInformation {

    @IDatabaseField(columnName = "id",save = false,isID = true)
    public int id;

    @IDatabaseField(columnName = "information",fromJSON = true)
    public JavaDocIfoJSON json;

}
