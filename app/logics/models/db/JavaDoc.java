package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;
import logics.databaseUtilities.Setter;
import logics.models.db.information.JavaDocIfoJSON;
import logics.models.db.information.TransverseInformation;

/**
 * Created by bedux on 25/03/16.
 */
@IDatabaseClass(tableName = "JavaDoc",idName = "id")
public class JavaDoc extends TransverseInformation {

    public long getId() {
        return id;
    }



    public JavaDocIfoJSON getJson() {
        return json;
    }

    @Setter
    public void setJson(JavaDocIfoJSON json) {
        this.json = json;
    }

    @IDatabaseField(columnName = "id", save = false, isID = true)
    private long id;

    @IDatabaseField(columnName = "information", fromJSON = true)
    private JavaDocIfoJSON json;



}
