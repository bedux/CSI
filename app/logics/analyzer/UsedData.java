package logics.analyzer;

import logics.models.newDatabase.Cache;
import logics.models.newDatabase.JavaImport;
import logics.models.newDatabase.JavaMethodCall;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by bedux on 20/05/16.
 */
public class UsedData {
    private static  UsedData instance = new UsedData();

    final HashMap<String,JavaImport> cacheJavaImport = new HashMap<>();
    final HashMap<String,JavaMethodCall> cacheJavaMethod = new HashMap<>();

    /**
     * Clear the cache
     */
    public void clear(){
        cacheJavaImport.clear();
        cacheJavaMethod.clear();
    }

    /**
     *
     * @return the current instance
     */
    public synchronized static UsedData getInstance(){
        return instance;
    }

    /**
     * Consturctor, Inizialize the two lists
     */
    private UsedData(){
        final List<JavaImport> result = JavaImport.find.all();
        result.stream().forEach(x-> cacheJavaImport.put(x.packageName,x));

        final List<JavaMethodCall> hmch = JavaMethodCall.find.all();
        hmch.stream().forEach(x-> cacheJavaMethod.put(computeHashMehtod(x.methodname,x.params),x));
    }

    /**
     * Compute an hash for the methods
     * @param name
     * @param par
     * @return
     */
    private synchronized String computeHashMehtod(String name,int par){
        return name + "#"+par;
    }


    /**
     * Get the import if exist
     * @param pac
     * @return
     */
    public synchronized Optional<JavaImport> getImport(String pac){
        pac = pac.trim();

        if(cacheJavaImport.containsKey(pac)){
            return Optional.of(cacheJavaImport.get(pac));
        }
        return Optional.empty();
    }


    /***
    * Get the methods if exist

     * @param call
     * @param par
     * @return
     */
    public synchronized Optional<JavaMethodCall> getMethodCall(String call,int par){
        call = call.trim();
        if(cacheJavaMethod.containsKey(computeHashMehtod(call,par))){
            return Optional.of(cacheJavaMethod.get(computeHashMehtod(call,par)));
        }
        return Optional.empty();
    }

    /**
     * Add a new Import
     * @param pac
     */
    public synchronized void addImport(JavaImport pac){
        cacheJavaImport.put(pac.packageName.trim(),pac);
    }

    /**
     * Add a method call. Just important to know!
     * @param cc
     */
    public synchronized void addMethodCall(JavaMethodCall cc){
        cacheJavaMethod.put(computeHashMehtod(cc.methodname.trim(),cc.params),cc);
    }

    public static Object loock = new Object();


    /**
     * This function wrap a query into a cache query. You need to pass the getter and setter for the cache table
     * @param func
     * @param getter
     * @param setter
     * @return
     */
    public static   Function<String,Long> wrapCache(Function<String,Long> func,Function<Cache,Long> getter,BiConsumer<Cache,Long> setter){
        return (x)->{
            Cache c ;
                synchronized (loock) {
                     c = (Cache) Cache.find.byId(x);
                    if (c == null) {
                       c = new Cache(x);
                        c.save();
                    }
                }

                Long val = getter.apply(c);
                if (val.longValue() == -1) {
                    Long res = func.apply(x);
                    setter.accept(c, res);
                    return res;

                } else {
                    return val;
                }

        };

    }

}
