package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;
import logics.databaseUtilities.ManyToOne;
import logics.databaseUtilities.Setter;

/**
 * Created by bedux on 12/04/16.
 */

@IDatabaseClass(tableName = "repositoryrender")
public class RepositoryRender extends BaseTable {

    @IDatabaseField(columnName = "id", save = false, isID = true)
    private long id;

    @IDatabaseField(columnName = "repositoryversion")
    private long repositoryVersion;

    @Setter
    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }
    @Setter
    public void setMetricType(String metricType) {
        this.metricType = metricType;
    }

    @IDatabaseField(columnName = "localpath")
    private String localPath;


    @IDatabaseField(columnName = "metrictype")
    private String metricType;

    @IDatabaseField(columnName = "repositoryid")
    private long repositoryId;


    public    RepositoryVersion repositoryVersionConcrete;
    @ManyToOne(columnName = "repositoryVersionConcrete",columnNameRefTable = "repositoryversion")
    public RepositoryVersion getRepositoryVersion() {
        return repositoryVersionConcrete;
    }


    public    Repository repositoryConcrete;
    @ManyToOne(columnName = "repositoryConcrete",columnNameRefTable = "repositoryId")
    public Repository getrepositoryConcrete() {
        return repositoryConcrete;
    }


    @Setter
    public void setrepositoryConcrete(Repository r){
        this.repositoryId = r.getId();
        repositoryConcrete = r;
    }

    @Setter
    public void setRepositoryVersionConcrete(RepositoryVersion r){
        this.repositoryVersion = r.getId();
        repositoryVersionConcrete = r;
    }


    public String getNameToDisplay() {
        return nameToDisplay;
    }

    public void setNameToDisplay(String nameToDisplay) {
        this.nameToDisplay = nameToDisplay;
    }

    public long getNumberOfFileToDispaly() {
        return numberOfFileToDispaly;
    }

    public void setNumberOfFileToDispaly(long numberOfFileToDispaly) {
        this.numberOfFileToDispaly = numberOfFileToDispaly;
    }



    public String getLocalPath() {
        return localPath;
    }

    public String getMetricType() {
        return metricType;
    }



    private String nameToDisplay="";

    private long numberOfFileToDispaly = 0L;

    private  String nameOfMetrix = "";
}


