package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;

/**
 * Created by bedux on 25/03/16.
 */
@IDatabaseClass(tableName = "TransverseInformation",idName = "id_TI")
public class TransverseInformation {

    @IDatabaseField(columnName = "id_TI", save = false, isID = true)
    public long id;

    @IDatabaseField(columnName = "ContainsTransverseInformation")
    public long containsTransverseInformation;


}
