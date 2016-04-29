package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;
import logics.databaseUtilities.ManyToOne;
import logics.databaseUtilities.Setter;
import logics.models.db.information.JavaImportInformation;


@IDatabaseClass(tableName = "JavaImport",idName = "id")
public class JavaImport extends JavaSourceObject {


    @IDatabaseField(columnName = "id", save = false, isID = true)
    private long id;


    @IDatabaseField(columnName = "information", fromJSON = true)
    private JavaImportInformation json;

    @Override
    public long getId() {
        return id;
    }



    public JavaImportInformation getJson() {
        return json;
    }

    @Setter
    public void setJson(JavaImportInformation json) {
        this.json = json;
    }
}
