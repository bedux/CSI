package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;
import logics.models.db.information.JavaImportInformation;


@IDatabaseClass(tableName = "JavaImport",idName = "id")
public class JavaImport extends JavaSourceObject {


    @IDatabaseField(columnName = "id", save = false, isID = true)
    public long id;


    @IDatabaseField(columnName = "information", fromJSON = true)
    public JavaImportInformation json;

}
