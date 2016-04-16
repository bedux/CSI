package logics.models.query;

/**
 * Created by bedux on 29/03/16.
 */
public class ComputeWithSingleQuery implements IComputeAttributeContainer {

    QueryWithPath countQuery;

    public ComputeWithSingleQuery(QueryWithPath countQuery) {
        this.countQuery = countQuery;
    }

    @Override
    public long executeAndGetResult(String path) {
        countQuery.setPath(path);
        return this.countQuery.executeAndGetFirst().getCount();
    }

    @Override
    public long executeAndGetResult(long id) {
        countQuery.setPath(id);
        return this.countQuery.executeAndGetFirst().getCount();
    }

    @Override
    public ComputeWithSingleQuery clone() {
        return new ComputeWithSingleQuery(countQuery.clone());
    }
}
