package logics.models.query;

import exception.CustomException;
import javafx.util.Pair;

import javax.persistence.Tuple;
import java.util.function.Function;

/**
 * Created by bedux on 29/03/16.
 */
public  class ComputeProportionOfTwoQuery <T,T1> implements Function<Pair<T,T1>,Long> {

    private final Function<T,Long> q1;
    private final Function<T1,Long> q2;

    /***
     *
     * @param firstQueryTotal query A
     * @param secondQuery query B
     */
    public ComputeProportionOfTwoQuery( Function<T,Long> firstQueryTotal, Function<T1,Long> secondQuery) {
        q1 = firstQueryTotal;
        q2 = secondQuery;
    }

//    /***
//     * Using path get the result
//     * @param path
//     * @return
//     */
//    @Override
//    public long executeAndGetResult(String path) {
//
//        long a1 = q1.executeAndGetResult(path);
//        long a2 = q2.executeAndGetResult(path);
//        if(a1==0) return  0 ;
//        float result = (((float)(a2) / (float)(a1))*100f);
//
//        if(a1<a2){
//            throw new CustomException("Wrong percentage >"+result+" "+a1 +  " " + a2+" "+path);
//         }
//
//        return (long)(result);
//    }
//
//    /***
//     * Using the id get the result
//     * @param id
//     * @return
//     */
//    @Override
//    public long executeAndGetResult(long id) {
//
//        long a1 = q1.executeAndGetResult(id);
//        long a2 = q2.executeAndGetResult(id);
//        if(a1==0) return  0 ;
//        float result = (((float)(a2) / (float)(a1))*100f);
//        if(a1<a2){
//            throw new CustomException("Wrong percentage >"+result+" "+a1 + " "+a2);
//        }
//
//        return (long)(result);
//    }




    @Override
    public Long apply(Pair<T, T1> param) {
        long a1 = q1.apply(param.getKey());
        long a2 = q2.apply(param.getValue());
        if(a1==0) return  0L ;
        float result = (((float)(a2) / (float)(a1))*100f);
        if(a1<a2){
            throw new CustomException("Wrong percentage >"+result+" "+a1 + " "+a2);
        }

        return (long)(result);
    }
}
