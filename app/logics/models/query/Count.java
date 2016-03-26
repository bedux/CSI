package logics.models.query;

import logics.databaseUtilities.IDatabaseField;

/**
 * Created by bedux on 26/03/16.
 */
public class Count {
    @IDatabaseField(columnId = 1)
    public long count;
}
