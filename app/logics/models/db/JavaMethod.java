package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;
import logics.databaseUtilities.ManyToOne;
import logics.databaseUtilities.Setter;
import logics.models.db.information.MethodInfoJSON;


@IDatabaseClass(tableName = "JavaMethod",idName = "id")
public class JavaMethod extends JavaSpecificComponent {

    @IDatabaseField(columnName = "id", save = false, isID = true)
    private long id;


    @IDatabaseField(columnName = "information", fromJSON = true)
    private MethodInfoJSON json;



    public long getId() {
        return id;
    }


    public MethodInfoJSON getJson() {
        return json;
    }

    @Setter
    public void setJson(MethodInfoJSON json) {
        this.json = json;
    }


}
