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
    final HashMap<String,JavaImport> chacheJavaImport = new HashMap<>();
    final HashMap<String,JavaMethodCall>chacheJavaMethod = new HashMap<>();


    public synchronized static UsedData getInstance(){
        return instance;
    }

    private UsedData(){
        final List<JavaImport> result = JavaImport.find.all();
        result.stream().forEach(x->chacheJavaImport.put(x.packageName,x));

        final List<JavaMethodCall> hmch = JavaMethodCall.find.all();
        hmch.stream().forEach(x->chacheJavaMethod.put(computeHashMehtod(x.methodname,x.params),x));
    }

    private synchronized String computeHashMehtod(String name,int par){
        return name + "#"+par;
    }


    public synchronized Optional<JavaImport> getImport(String pac){
        pac = pac.trim();

        if(chacheJavaImport.containsKey(pac)){
            return Optional.of(chacheJavaImport.get(pac));
        }
        return Optional.empty();
    }



    public synchronized Optional<JavaMethodCall> getMethodCall(String call,int par){
        call = call.trim();
        if(chacheJavaMethod.containsKey(computeHashMehtod(call,par))){
            return Optional.of(chacheJavaMethod.get(computeHashMehtod(call,par)));
        }
        return Optional.empty();
    }

    public synchronized void addImport(JavaImport pac){
        chacheJavaImport.put(pac.packageName.trim(),pac);
    }

    public synchronized void addMethodCall(JavaMethodCall cc){
        chacheJavaMethod.put(computeHashMehtod(cc.methodname.trim(),cc.params),cc);
    }

    public static Object loock = new Object();

    public static   Function<String,Long> wrapCache(Function<String,Long> func,Function<Cache,Long> getter,BiConsumer<Cache,Long> setter){
        return (x)->{
            synchronized (loock) {
                Cache c = (Cache) Cache.find.byId(x);
                if (c == null) {
                    Long res = func.apply(x);
                    Cache nC = new Cache(x);
                    nC.save();
                    setter.accept(nC, res);
                    nC.update();
                    return res;


                }
                Long val = getter.apply(c);
                if (val.longValue() == -1) {
                    Long res = func.apply(x);
                    setter.accept(c, res);
                    c.update();

                    return res;

                } else {
                    return val;
                }
            }
        };

    }

}
