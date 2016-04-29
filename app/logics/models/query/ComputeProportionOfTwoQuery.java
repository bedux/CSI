package logics.models.query;

import exception.CustomException;
import javafx.util.Pair;
import logics.models.modelQuery.IQuery;

import javax.persistence.Tuple;

/**
 * Created by bedux on 29/03/16.
 */
public  class ComputeProportionOfTwoQuery <T,T1> implements IQuery<Pair<T,T1>,Long> {

    private final IQuery<T,Long> q1;
    private final IQuery<T1,Long> q2;

    /***
     *
     * @param firstQueryTotal query A
     * @param secondQuery query B
     */
    public ComputeProportionOfTwoQuery( IQuery<T,Long> firstQueryTotal, IQuery<T1,Long> secondQuery) {
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
    public ComputeProportionOfTwoQuery clone() {
        return new ComputeProportionOfTwoQuery(q1.clone(),q2.clone());
    }


    @Override
    public Long execute(Pair<T, T1> param) {
        long a1 = q1.execute(param.getKey());
        long a2 = q2.execute(param.getValue());
        if(a1==0) return  0L ;
        float result = (((float)(a2) / (float)(a1))*100f);
        if(a1<a2){
            throw new CustomException("Wrong percentage >"+result+" "+a1 + " "+a2);
        }

        return (long)(result);
    }
}
