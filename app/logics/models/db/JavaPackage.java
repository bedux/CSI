package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;
import logics.models.db.information.JavaPackageInformation;



@IDatabaseClass(tableName = "JavaPackage")
public class JavaPackage extends JavaSourceObject {

    @IDatabaseField(columnName = "id", save = false, isID = true)
    public long id;

    @IDatabaseField(columnName = "information", fromJSON = true)
    public JavaPackageInformation json;

}
