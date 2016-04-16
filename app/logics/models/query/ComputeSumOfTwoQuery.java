package logics.models.query;

/**
 * Created by bedux on 29/03/16.
 */
public class ComputeSumOfTwoQuery implements IComputeAttributeContainer {
    IComputeAttributeContainer q1;
    IComputeAttributeContainer q2;

    public ComputeSumOfTwoQuery(IComputeAttributeContainer firstQueryTotal, IComputeAttributeContainer secondQuery) {
        q1 = firstQueryTotal;
        q2 = secondQuery;
    }

    @Override
    public long executeAndGetResult(String path) {

        long a1 = q1.executeAndGetResult(path);
        long a2 = q2.executeAndGetResult(path);
        return a2 + a1;
    }

    @Override
    public long executeAndGetResult(long id) {

        long a1 = q1.executeAndGetResult(id);
        long a2 = q2.executeAndGetResult(id);
        return a2 + a1;
    }

    @Override
    public ComputeSumOfTwoQuery clone() {
        return new ComputeSumOfTwoQuery(q1.clone(),q2.clone());
    }
}
