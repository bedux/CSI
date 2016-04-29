package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;
import logics.databaseUtilities.ManyToOne;
import logics.databaseUtilities.Setter;
import logics.models.db.information.JavaEnumInformation;
import logics.models.db.information.MethodInfoJSON;

/**
 * Created by bedux on 31/03/16.
 */
@IDatabaseClass(tableName = "JavaEnum")
public class JavaEnum extends JavaSourceObject  {

    @IDatabaseField(columnName = "id", save = false, isID = true)
    private long id;

    @Override
    public long getId() {
        return id;
    }

    @Setter
    public void setJson(JavaEnumInformation json) {
        this.json = json;
    }

    public JavaEnumInformation getJson() {
        return json;
    }

    @IDatabaseField(columnName = "information", fromJSON = true)
    private JavaEnumInformation json;






}
