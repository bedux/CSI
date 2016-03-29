package logics.models.query;

import logics.databaseUtilities.IDatabaseField;

/**
 * Created by bedux on 26/03/16.
 */
public class CountQuery {
    @IDatabaseField(columnId = 1)
    public long count = 0;

    public long getCount() {
        return count;
    }
}
