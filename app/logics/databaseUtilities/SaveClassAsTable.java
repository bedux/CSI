package logics.databaseUtilities;

import exception.CustomException;
import logics.DatabaseManager;
import org.postgresql.util.PGobject;
import play.libs.Json;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Update and Save Table in Database
 */
public class SaveClassAsTable {
    private String clear(String s) {
        return s.replaceAll("[\u0000]", "");
    }

    /***
     *
     * @param type class to analyse
     * @return all field of the class, and also inherited fields
     */
    public static List<Field> getInheritedFields(Class<?> type) {
        List<Field> result = new ArrayList<Field>();

        Class<?> i = type;
        while (i != null && i != Object.class) {
            result.addAll(Arrays.asList(i.getDeclaredFields()));
            i = i.getSuperclass();
        }
        return result;
    }

    /***
     *
     * This Funcion is used for save object @See IDatabaseClass and @See IDatabaseField
     * @param object to save, Use the @see IDatabaseClass
     * @param <T> the class element to be saved and  return
     * @return the saved element
     */
    public <T> Integer save(T object) {
        try {
            IDatabaseClass annotationClass = object.getClass().getAnnotation(IDatabaseClass.class);
            if (annotationClass == null || annotationClass.tableName() == "") {
                throw new CustomException("No table name found in your class declaration!");
            }
            String insertQuery = "INSERT INTO " + annotationClass.tableName() + " (";
            String filedValue = "(";
            HashMap<Integer, Object> param = new HashMap<>();
            int i = 1;
            for (Field f : getInheritedFields(object.getClass())) {
                IDatabaseField idbc = f.getAnnotation(IDatabaseField.class);
                if (idbc != null && idbc.save() && f.get(object) != null) {
                    insertQuery += " " + idbc.columnName() + ",";

                    if (idbc.fromJSON()) {
                        PGobject p = new PGobject();
                        String json = Json.stringify(Json.toJson(f.get(object)));
                        json = Normalizer.normalize(json, Normalizer.Form.NFC);
                        p.setValue(json);
                        p.setType("jsonb");
                        param.put(i, p);
                        filedValue += "?, ";
                        i++;

                    } else if (f.getType().isPrimitive()) {
                        param.put(i, f.get(object));
                        filedValue += "?, ";
                        i++;

                    } else {
                        param.put(i, f.get(object));
                        filedValue += "?, ";
                        i++;

                    }
                }

            }
            insertQuery = insertQuery.substring(0, insertQuery.lastIndexOf(","));
            filedValue = filedValue.substring(0, filedValue.lastIndexOf(","));

            insertQuery += ") VALUES " + filedValue + ") RETURNING "+annotationClass.idName();
            return DatabaseManager.getInstance().makeSaveQuery(insertQuery, param);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(e);
        }
    }


    /***
     *
     * @param object object to be update
     * @param <T> Type of the object
     * @throws IllegalAccessException
     * @throws SQLException
     * @throws InstantiationException
     */
    public <T> void update(T object) throws IllegalAccessException, SQLException, InstantiationException {
        IDatabaseClass annotationClass = object.getClass().getAnnotation(IDatabaseClass.class);
        if (annotationClass.tableName() == "") {
            throw new CustomException("No table name found in your class declaration!");
        }
        String insertQuery = "UPDATE " + annotationClass.tableName() + " SET ";
        HashMap<Integer, Object> param = new HashMap<>();
        Object id = null;
        String idName = "id";
        int i = 1;

        for (Field f : getInheritedFields(object.getClass())) {
            IDatabaseField idbc = f.getAnnotation(IDatabaseField.class);
            if (idbc != null && idbc.save()) {
                insertQuery += " " + idbc.columnName() + " = ";

                if (idbc.fromJSON()) {
                    String json =clear( Json.stringify(Json.toJson(f.get(object))));
                    param.put(i, json);
                    insertQuery += "?::jsonb, ";
                    i++;

                } else if (f.getType().isPrimitive()) {
                    param.put(i, f.get(object));
                    insertQuery += "?, ";
                    i++;

                } else {
                    param.put(i, f.get(object));
                    insertQuery += "?, ";
                    i++;

                }
            } else if (idbc != null && idbc.isID()) {
                id = f.get(object);
            }
        }
        insertQuery = insertQuery.substring(0, insertQuery.lastIndexOf(","));

        if (id != null) {
            insertQuery += " WHERE "+ annotationClass.idName() +" = ?";
            param.put(i, id);
        }
        DatabaseManager.getInstance().makeUpdateQuery(insertQuery, param);
    }
}
