package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;

/**
 * Created by bedux on 16/05/16.
 */
@IDatabaseClass(tableName = "methodcountview")
public class MethodCountView {

    @IDatabaseField(columnName = "id", save = false, isID = true)
    private long id;

    @IDatabaseField(columnName = "methodName")
    private String methodName="";

    @IDatabaseField(columnName = "number")
    private long number;

    public long getNumber() {
        return number;
    }

    public String getMethodName() {
        return methodName;
    }
}
