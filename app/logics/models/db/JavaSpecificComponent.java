package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;


@IDatabaseClass(tableName = "JavaSpecificComponent")
public class JavaSpecificComponent {

    @IDatabaseField(columnName = "id",save = false,isID = true)
    public int id;

    @IDatabaseField(columnName = "javaSource")
    public int javaSource;



}
