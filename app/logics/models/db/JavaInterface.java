package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;
import logics.databaseUtilities.ManyToOne;
import logics.databaseUtilities.Setter;
import logics.models.db.information.MethodInfoJSON;


@IDatabaseClass(tableName = "JavaInterface",idName = "id")
public class JavaInterface extends JavaSourceObject {


    @IDatabaseField(columnName = "id", save = false, isID = true)
    private long id;



    public MethodInfoJSON getJson() {
        return json;
    }

    @Setter
    public void setJson(MethodInfoJSON json) {
        this.json = json;
    }

    @IDatabaseField(columnName = "information", fromJSON = true)
    private MethodInfoJSON json;



  

    @Override
    public long getId() {
        return id;
    }
}
