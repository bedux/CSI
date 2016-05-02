package logics.models.query;

import javafx.util.Pair;
import java.util.function.Function;

/**
 * Created by bedux on 29/03/16.
 */
public class ComputeSumOfTwoQuery<T,T1> implements Function<Pair<T,T1>,Long> {
    Function<T,Long> q1;
    Function<T1,Long> q2;

    public ComputeSumOfTwoQuery( Function<T,Long> firstQueryTotal,  Function<T1,Long> secondQuery) {
        q1 = firstQueryTotal;
        q2 = secondQuery;
    }

    @Override
    public Long apply(Pair<T, T1> param) {
        long a1 = q1.apply(param.getKey());
        long a2 = q2.apply(param.getValue());
        return a2 + a1;
    }


}
