package logics.databaseUtilities;

import org.postgresql.util.PGobject;
import play.libs.Json;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by bedux on 25/03/16.
 */
public class ConvertTableToClass {

    private ResultSet resultSet;

    public ConvertTableToClass(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    /**
     * @param resultClass The class type of the result
     * @param <T>         the result type
     * @return the create T object
     * @throws SQLException
     * @throws IllegalAccessException
     */
    public <T> T convert(Class<T> resultClass) throws SQLException, IllegalAccessException, InstantiationException {

        T returnValue = resultClass.newInstance();

        for (Field f : SaveClassAsTable.getInheritedFields(resultClass)) {
            IDatabaseField annotation = f.getAnnotation(IDatabaseField.class);
            if (!annotation.fromJSON() && annotation.columnId() == -1) {
                f.set(returnValue, getObjectFromColumnIndex(getColumnIndexFromName(annotation.columnName())));
            } else if (!annotation.fromJSON() && annotation.columnId() != -1) {
                f.set(returnValue, getObjectFromColumnIndex(annotation.columnId()));
            } else {
                PGobject obj = (PGobject) getObjectFromColumnIndex(getColumnIndexFromName(annotation.columnName()));
                if (obj != null) {
                    f.set(returnValue, Json.fromJson(Json.parse(obj.getValue()), f.getType()));
                }
            }
        }
        return returnValue;
    }

    /**
     * @param columnName name of the column
     * @return the index of the column
     * @throws SQLException
     */
    private int getColumnIndexFromName(String columnName) throws SQLException {
        return resultSet.findColumn(columnName);
    }

    /**
     * @param index the index of the column
     * @return the object
     * @throws SQLException
     */
    private Object getObjectFromColumnIndex(int index) throws SQLException {
        return resultSet.getObject(index);

    }

}
