package logics.models.modelQuery;

/**
 * Created by bedux on 29/04/16.
 */
public interface IQuery<In,Out> {
    Out execute(In param);
    IQuery<In,Out> clone();
}
