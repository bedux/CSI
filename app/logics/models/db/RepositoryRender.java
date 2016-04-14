package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;

/**
 * Created by bedux on 12/04/16.
 */
@IDatabaseClass(tableName = "repositoryrender")
public class RepositoryRender {

    @IDatabaseField(columnName = "id", save = false, isID = true)
    public Integer id;

    @IDatabaseField(columnName = "repositoryversion")
    public Integer repositoryVersion;

    @IDatabaseField(columnName = "localpath")
    public String localPath;


    @IDatabaseField(columnName = "metrictype")
    public String metricType;

    @IDatabaseField(columnName = "repositoryid")
    public Integer repositoryId;

    public String nameToDisplay="";

    public Long numberOfFileToDispaly = 0L;

    public  String nameOfMetrix = "";

}


