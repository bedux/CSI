package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;


@IDatabaseClass(tableName = "JavaSpecificComponent",idName = "id_JSP")
public class JavaSpecificComponent {

    @IDatabaseField(columnName = "id_JSP", save = false, isID = true)
    public int id;

    @IDatabaseField(columnName = "javaSource")
    public int javaSource;


}
