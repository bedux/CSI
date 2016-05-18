package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;
import logics.databaseUtilities.SaveTable;

/**
 * Created by bedux on 16/05/16.
 */
@IDatabaseClass(tableName = "importcountview")
public class ImportCountView {

    @IDatabaseField(columnName = "id", save = false, isID = true)
    private long id;

    @IDatabaseField(columnName = "package")
    private String importz="";

    @IDatabaseField(columnName = "number")
    private long number;

    public long getNumber() {
        return number;
    }



    public String getImportz() {
        return importz;
    }
}
