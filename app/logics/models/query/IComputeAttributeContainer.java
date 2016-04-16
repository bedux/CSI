package logics.models.query;

import org.apache.commons.lang3.SerializationUtils;

/**
 * Created by bedux on 29/03/16.
 */
public interface IComputeAttributeContainer   {
    long executeAndGetResult(String path);
    long executeAndGetResult(long id);
    IComputeAttributeContainer clone();



}
