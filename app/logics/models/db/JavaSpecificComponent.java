package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;


@IDatabaseClass(tableName = "JavaSpecificComponent",idName = "id")
public class JavaSpecificComponent {

    @IDatabaseField(columnName = "id", save = false, isID = true)
    public long id;

    @IDatabaseField(columnName = "javaSource")
    public long javaSource;


}
