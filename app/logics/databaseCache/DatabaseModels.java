package logics.databaseCache;

import exception.CustomException;
import logics.DatabaseManager;
import logics.databaseUtilities.*;
import logics.models.db.JavaInterface;
import logics.models.db.JavaSourceObject;
import org.h2.engine.Database;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import javax.swing.text.html.Option;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by bedux on 22/04/16.
 */

class HandlerProxy<T> implements MethodInterceptor{

    final T obj;
    public  HandlerProxy(T object){
        this.obj = object;
    }

    private synchronized Long getId() throws IllegalAccessException {

        Field f = Stream.of(obj.getClass().getDeclaredFields()).filter(x -> x.getAnnotation(IDatabaseField.class) != null)
                    .filter(x->x.getAnnotation(IDatabaseField.class).isID()).findFirst().get();
        f.setAccessible(true);

        return (Long) f.get(obj);
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


    public static List<Method> getInheritedMethods(Class<?> type) {
        List<Method> result = new ArrayList<Method>();

        Class<?> i = type;
        while (i != null && i != Object.class) {
            result.addAll(Arrays.asList(i.getDeclaredMethods()));
            i = i.getSuperclass();
        }
        return result;
    }

    public static Method getInheritedMethods(Class<?> type,Method m) {

        Class<?> i = type;
        while (i != null && i != Object.class) {
            Optional<Method> me =Stream.of(i.getDeclaredMethods()).filter(x -> x.getName().equals(m.getName())).findFirst();
            if(me.isPresent()){
                return me.get();
            }
            i = i.getSuperclass();
        }
        return null;
    }

    public static Field getInerithFiled(String name,Class<?> type){
        return getInheritedFields(type).stream().filter(x->x.getName().equals(name)).findFirst().get();
    }


    @Override
    public   Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {


            method = getInheritedMethods(obj.getClass(),method);

            if(method==null) return null;
            if (method.getAnnotation(Setter.class) != null) {

                Object toReturn = methodProxy.invoke(obj, objects);
                new SaveClassAsTable().update(obj);
                return toReturn;
            } else if (method.getAnnotation(OneToMany.class) != null) {

                OneToMany annotation = method.getAnnotation(OneToMany.class);

                Object toReturn = methodProxy.invoke(obj, objects);
                if (toReturn != null) return toReturn;


                for (int i = 0; i < annotation.referTo().length; i++) {

                    String t1 = annotation.referTo()[i];

                    Field ta = getInerithFiled(t1, obj.getClass());

                    Class<?> c = (Class<?>) ((ParameterizedType) ta.getGenericType()).getActualTypeArguments()[0];

                    String tableName = c.getAnnotation(IDatabaseClass.class).tableName();


                    Method m = getInheritedMethods(c).stream().filter(x -> x.getAnnotation(ManyToOne.class) != null)
                            .filter(x -> x.getReturnType().equals(obj.getClass()) || x.getReturnType().equals(obj.getClass().getSuperclass())).findFirst().get();


                    String query = ("SELECT * FROM " + tableName + " WHERE " + tableName + "." + m.getAnnotation(ManyToOne.class).columnNameRefTable() + " = " + getId());


                    List<?> lists = DatabaseManager.getInstance().makeQuery(query, new HashMap<>(), c);

                    List<Object> result = new ArrayList<>();

                    for (Object objj : lists) {
                        Object proxed = DatabaseModels.getInstance().getEntity(objj).get();
                        Object proxedMy = DatabaseModels.getInstance().getEntity(obj.getClass(), getId()).get();

                        Field f = getInerithFiled(m.getAnnotation(ManyToOne.class).columnName(), objj.getClass());
                        f.setAccessible(true);
                        f.set(objj, obj);
                        result.add(proxed);


                    }
                    ta.setAccessible(true);

                    if (ta.get(obj) != null) {
                        result.addAll((Collection<?>) ta.get(obj));
                    } else {
                        ta.set(obj, result);
                    }

                }
                toReturn = methodProxy.invoke(obj, objects);

                return toReturn;

            } else if (method.getAnnotation(ManyToOne.class) != null) {

                Field f1 = getInerithFiled(method.getAnnotation(ManyToOne.class).columnName(), obj.getClass());
                f1.setAccessible(true);
                if (f1.get(obj) != null) {
                    return methodProxy.invoke(obj, objects);

                }

                Class<?> refClass = method.getReturnType();

                final Method method1= method;
                //try getting the id of the class
                Long id = getInheritedFields(obj.getClass()).stream()
                        .filter(x -> x.getAnnotation(IDatabaseField.class) != null && x.getAnnotation(IDatabaseField.class).columnName().equals(method1.getAnnotation(ManyToOne.class).columnNameRefTable()))
                        .map(x -> {
                            try {
                                x.setAccessible(true);
                                return x.getLong(obj);
                            } catch (IllegalAccessException e) {
                                throw new CustomException(e);
                            }
                        })
                        .findFirst().get();


                //  String query =  "SELECT * FROM  "+tableRefName+" where "+tableRefName+".id = "+ id;
                // System.out.println(query);
                //List<?> lists = DatabaseManager.ge
                // tInstance().makeQuery(query, new HashMap<>(), refClass);
                Object l = DatabaseModels.getInstance().getEntity(refClass, id);

                Optional<?> pi = (Optional<?>) l;

                if (!pi.isPresent()) {
                    DatabaseModels.getInstance().invalidCache();
                    l = DatabaseModels.getInstance().getEntity(refClass, id);
                    pi = (Optional<?>) l;
                    System.out.println("Try delete cache");
                    if (!pi.isPresent()) {
                        throw new CustomException("Database is Broken! " + getId() + "   " + obj.getClass().getName());
                    }
                }


                Field f3 = getInerithFiled(method.getAnnotation(ManyToOne.class).columnName(), obj.getClass());
                f3.setAccessible(true);
                f3.set(obj, pi.get());


                return methodProxy.invoke(obj, objects);

            } else {
                return methodProxy.invoke(obj, objects);
            }


    }
}




public class DatabaseModels {

    private synchronized Long getId(Object obj) throws IllegalAccessException {
        Field f = Stream.of(obj.getClass().getDeclaredFields()).filter(x -> x.getAnnotation(IDatabaseField.class) != null)
                .filter(x->x.getAnnotation(IDatabaseField.class).isID()).findFirst().get();
        f.setAccessible(true);

        return (Long) f.get(obj);
    }

    private HashMap<String,Object> tables = new HashMap<>();
    private HashMap<Class<?>,List<Object>> tablesGetAll = new HashMap<>();

    private SaveClassAsTable toolsDB = new SaveClassAsTable();

    private static DatabaseModels instance = new DatabaseModels();

    public static DatabaseModels getInstance(){

        return instance;
    }


    public synchronized <T> Optional<T>  getEntity(Class<T> tableType) {
        try {
            T table = tableType.newInstance();
            long id =  new SaveClassAsTable().save(table);
            Field idField = Stream.of(table.getClass().getDeclaredFields()).filter(x->x.getAnnotation(IDatabaseField.class) != null).filter(x->x.getAnnotation(IDatabaseField.class).isID()).findFirst().get();
            idField.setAccessible(true);
            idField.setLong(table, id);
            table = makeProxy(table);
            tables.put(computeHash(id,tableType),table);
            if(!tablesGetAll.containsKey(tableType)){
                getAll(tableType);
            }
            tablesGetAll.get(tableType).add(table);
            return Optional.of(table);

        }catch (Exception ee){
            throw  new CustomException(ee);
        }
    }


    public synchronized<T> T makeProxy(T o) {
        T fProxy =(T) Enhancer.create(o.getClass(),new HandlerProxy(o));
        return fProxy;
    }


    public synchronized<T> Optional<T> getEntity(Class<T> tableType,long id){
        String hash = computeHash(id,tableType);

        if(tables.containsKey(hash)){
            return Optional.of((T)tables.get(hash));
        }
        try {
            Optional<T> table = toolsDB.get(id, tableType);
            if(table.isPresent()) {
               T tab =  makeProxy(table.get());
                tables.put(hash,tab );
                if(!tablesGetAll.containsKey(tableType)){
                    getAll(tableType);
                }
                tablesGetAll.get(tableType).add(tab);
                return Optional.of(tab);
            }else{
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw  new CustomException(e);
        }
    }


    public synchronized<T> Optional<T> getEntity(T table){
        String hash = null;
        try {
            hash = computeHash(getId(table),table.getClass());
        } catch (IllegalAccessException e) {
            throw new CustomException(e);
        }

        if(tables.containsKey(hash)){
            return Optional.of((T)tables.get(hash));
        }else{
            T tab =  makeProxy(table);
            tables.put(hash,tab );
            if(!tablesGetAll.containsKey(table.getClass())){
                getAll(table.getClass());
            }
            tablesGetAll.get(table.getClass()).add(tab);
            return Optional.of(tab);
        }
    }


    public synchronized void invalidCache(){
        this.tables = new HashMap<>();
        this.tablesGetAll= new HashMap<>();
    }

    public synchronized<T> List<T> getAll(Class<T> className){
            if(tablesGetAll.containsKey(className)){
                return (List<T>)Collections.synchronizedList(tablesGetAll.get(className));
            }
            else{
                String query = "SELECT * FROM "+className.getAnnotation(IDatabaseClass.class).tableName();
                try {
                    List<T> result =  DatabaseManager.getInstance().makeQuery(query,new HashMap<>(),className);
                    final List<Object> toBeAdded = new ArrayList<>();
                    result.forEach(x->{
                            Optional<T> entry = getEntity(x);
                            toBeAdded.add(entry.get());
                            tablesGetAll.put(className,toBeAdded);
                    });
                    if(tablesGetAll.get(className)!=null) {
                        return (List<T>) Collections.synchronizedList(tablesGetAll.get(className));
                    }else{
                        return (List<T>) tablesGetAll.get(className);
                    }

                } catch (SQLException e) {
                   throw  new CustomException(e);
                }

            }
    }


//    /**
//     *
//     * @param id Identifier  of the class table to be loaded
//     * @param classToCreate type of the class toeate
//     * @param <T> Type to create
//     * @retur The table
//     */
//    public  <T extends VirtualTable> VirtualTable getTable(Long id,Class<T> classToCreate){
//
//        VirtualTable<T> result =  tables.computeIfAbsent(computeHash(id, classToCreate), (l) -> {
//            try {
//                return classToCreate.newInstance().load(id);
//            } catch (InstantiationException e) {
//                throw new CustomException(e);
//            } catch (IllegalAccessException e) {
//                throw new CustomException(e);
//            }
//
//        });
//        return result;
//    }
//
//    /***
//     *
//     * @param toPut save current class on the db. The id will be computed
//     * @param classToCreate class to be insert
//     * @param <T>   Type of the class
//     * @return  the inserted class
//     */
//    public  <T extends VirtualTable> VirtualTable putTable(T toPut,Class<T> classToCreate){
//        toPut.save();
//        return tables.put(computeHash(toPut.getId(),classToCreate),toPut);
//    }
//
//
//



    public synchronized String computeHash(Long id,Class<?> classToCreate){
        return id.toString()+"_"+classToCreate.getName();
    }



}
