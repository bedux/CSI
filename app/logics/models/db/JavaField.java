package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;
import logics.databaseUtilities.ManyToOne;
import logics.databaseUtilities.Setter;
import logics.models.db.information.FieldsInfoJSON;


@IDatabaseClass(tableName = "JavaField",idName = "id")
public class JavaField extends JavaSpecificComponent {

    @IDatabaseField(columnName = "id", save = false, isID = true)
    private long id;


    @IDatabaseField(columnName = "information", fromJSON = true)
    private FieldsInfoJSON json;

    public long getId() {
        return id;
    }

    public FieldsInfoJSON getJson() {
        return json;
    }

    @Setter
    public void setJson(FieldsInfoJSON json) {
        this.json = json;
    }


}
