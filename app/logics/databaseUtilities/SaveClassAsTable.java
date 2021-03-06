package logics.databaseUtilities;

import exception.CustomException;
import logics.DatabaseManager;
import org.postgresql.util.PGobject;
import play.libs.Json;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.*;

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
     *check if the class has a tableName
     * @param annotationClass  class annotation
     */
    private void checkIfIsAValidClass(IDatabaseClass annotationClass){
        if (annotationClass == null || annotationClass.tableName() == "") {
            throw new CustomException("No table name found in your class declaration!");
        }
    }

    /***
     *
     * This Funcion is used for save object @See IDatabaseClass and @See IDatabaseField
     * @param object to save, Use the @see IDatabaseClass
     * @param <T> the class element to be saved and  return
     * @return the saved element
     */
    public synchronized <T> Long save(T object) {
        try {
            IDatabaseClass annotationClass = object.getClass().getAnnotation(IDatabaseClass.class);
            checkIfIsAValidClass(annotationClass);
            String insertQuery = "INSERT INTO " + annotationClass.tableName() + " (";
            String filedValue = "(";
            HashMap<Integer, Object> param = new HashMap<>();
            int i = 1;
            for (Field f : getInheritedFields(object.getClass())) {
                f.setAccessible(true);
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
    public synchronized <T> void update(T object) throws IllegalAccessException, SQLException, InstantiationException {

        IDatabaseClass annotationClass = object.getClass().getAnnotation(IDatabaseClass.class);
        checkIfIsAValidClass(annotationClass);

        String insertQuery = "UPDATE " + annotationClass.tableName() + " SET ";
        HashMap<Integer, Object> param = new HashMap<>();
        Object id = null;
        int i = 1;

        for (Field f : getInheritedFields(object.getClass())) {
            f.setAccessible(true);

            IDatabaseField idbc = f.getAnnotation(IDatabaseField.class);
            if (idbc != null && idbc.save()) {
                insertQuery += " " + idbc.columnName() + " = ";
                if (idbc.fromJSON() && f.get(object)!=null) {

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
                if((long)f.get(object)!=0) {
                    id = f.get(object);
                }

            }
        }
        insertQuery = insertQuery.substring(0, insertQuery.lastIndexOf(","));

        if (id != null) {
            insertQuery += " WHERE "+ annotationClass.idName() +" = ?";
            param.put(i, id);
        }


        DatabaseManager.getInstance().makeUpdateQuery(insertQuery, param);
    }



    /***
     * Get a table by id
     * @param id of the table to get
     * @param type the type of the table
     * @param <T> The return type
     * @return the element
     * @throws SQLException
     */
    public synchronized <T> Optional<T> get(long id,Class<T> type) throws SQLException {
        IDatabaseClass annotationClass = type.getAnnotation(IDatabaseClass.class);
        checkIfIsAValidClass(annotationClass);

        String getQuery = "SELECT * FROM " + annotationClass.tableName() + " WHERE "+annotationClass.idName()+" = ? LIMIT 1";
        List<T> res =   DatabaseManager.getInstance().makeQuery(getQuery, new HashMap<Integer,Object>(){{put(1,id);}},type);
        if(res.size()>0){
            return Optional.of(res.get(0));
        }else{
            return Optional.empty();
        }
    }


//    public <T>  void updateJsonField(String columnName,String path,T newObject,Long objectId,Class<T> type){
//
//        try {
//            T table = get(objectId,type);
//            for(Field f:table.getClass().getFields()){
//                IDatabaseField idat = f.getAnnotation(IDatabaseField.class);
//                if(idat!=null && idat.columnName() == columnName){
//                    JsonNode res= Json.toJson(f.get(table));
//                }
//            }
//            String query = "UPDATE " + tableName + " SET "+columnName+" = jsonb_set(" + columnName + ", '" + path + "', '" + Json.stringify(Json.toJson(newObject)) + "', true) where id = " + objectId;
//            DatabaseManager.getInstance().makeUpdateQuery(query, new HashMap<Integer, Object>());
//        }catch  (SQLException e){
//            throw new CustomException(e);
//        } catch (IllegalAccessException e) {
//            throw new CustomException(e);
//        }
//
//    }

}
