package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;
import logics.databaseUtilities.ManyToOne;
import logics.databaseUtilities.Setter;
import logics.models.db.information.JavaImportInformation;
import logics.models.db.information.JavaPackageInformation;



@IDatabaseClass(tableName = "JavaPackage")
public class JavaPackage extends JavaSourceObject {

    @IDatabaseField(columnName = "id", save = false, isID = true)
    private long id;

    @IDatabaseField(columnName = "information", fromJSON = true)
    private JavaPackageInformation json;


    public JavaPackageInformation getJson() {
        return json;
    }

    @Setter
    public void setJson(JavaPackageInformation json) {
        this.json = json;
    }
}
