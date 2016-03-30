package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;

/**
 * Created by bedux on 25/03/16.
 */
@IDatabaseClass(tableName = "File")
public class File {
    @IDatabaseField(columnName = "id", save = false, isID = true)
    public long id;

    @IDatabaseField(columnName = "path")
    public String path;

    @IDatabaseField(columnName = "name")
    public String name;

    @IDatabaseField(columnName = "size")
    public int size;

    @IDatabaseField(columnName = "repositoryVersionId")
    public int repositoryVersionId;

}
