package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;
import logics.databaseUtilities.Setter;
import logics.models.db.information.JavaFileInformation;


@IDatabaseClass(tableName = "BinaryFile" ,idName = "id")
public class BinaryFile extends File {


    @IDatabaseField(columnName = "id", save = false, isID = true)
    private long id;


    @IDatabaseField(columnName = "information", fromJSON = true)
    private JavaFileInformation json;

    @Override
    public long getId() {
        return id;
    }



    public JavaFileInformation getJson() {
        return json;
    }

    @Setter
    public void setJson(JavaFileInformation json) {
        this.json = json;
    }
}
