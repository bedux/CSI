package logics.models.query;

import javafx.util.Pair;
import logics.models.modelQuery.IQuery;

/**
 * Created by bedux on 29/03/16.
 */
public class ComputeSumOfTwoQuery<T,T1> implements IQuery<Pair<T,T1>,Long> {
    IQuery<T,Long> q1;
    IQuery<T1,Long> q2;

    public ComputeSumOfTwoQuery( IQuery<T,Long> firstQueryTotal,  IQuery<T1,Long> secondQuery) {
        q1 = firstQueryTotal;
        q2 = secondQuery;
    }

    @Override
    public Long execute(Pair<T, T1> param) {
        long a1 = q1.execute(param.getKey());
        long a2 = q2.execute(param.getValue());
        return a2 + a1;
    }

    @Override
    public ComputeSumOfTwoQuery<T,T1> clone() {
        return new ComputeSumOfTwoQuery<T,T1>(q1.clone(),q2.clone());
    }
}
