package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;

@IDatabaseClass(tableName = "RepositoryVersion")
public class RepositoryVersion {
    @IDatabaseField(columnName = "id", save = false, isID = true)
    public Integer id;

    @IDatabaseField(columnName = "localPath")
    public String localPath;

    @IDatabaseField(columnName = "repositoryId")
    public Integer repositoryId;

}
