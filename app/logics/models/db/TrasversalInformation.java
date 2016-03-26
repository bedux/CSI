package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;

/**
 * Created by bedux on 25/03/16.
 */
@IDatabaseClass(tableName = "TrasversalInformation")
public class TrasversalInformation {

    @IDatabaseField(columnName = "id",save = false,isID = true)
    public int id;

    @IDatabaseField(columnName = "ContainsTransversalInformation")
    public int containsTransversalInformation;


}
