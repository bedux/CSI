package logics.models.db;

import logics.databaseUtilities.*;
import logics.models.db.information.MethodInfoJSON;

import java.util.List;

/**
 *
 * []-------->[]
 * List     Instance
 *
 *
 *
 */


@IDatabaseClass(tableName = "JavaClass")
public class JavaClass extends JavaSourceObject {


    @IDatabaseField(columnName = "id", save = false, isID = true)
    private long id;


    @IDatabaseField(columnName = "information", fromJSON = true)
    private MethodInfoJSON json;


    @Override
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
