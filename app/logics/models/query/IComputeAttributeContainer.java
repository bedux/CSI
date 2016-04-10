package logics.models.query;

/**
 * Created by bedux on 29/03/16.
 */
public interface IComputeAttributeContainer  {
    long executeAndGetResult(String path);
    long executeAndGetResult(long id);



}
