package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;
import logics.databaseUtilities.ManyToOne;
import logics.databaseUtilities.Setter;

/**
 * Created by bedux on 25/03/16.
 */
@IDatabaseClass(tableName = "File")
public class File extends BaseTable{

    @IDatabaseField(columnName = "id", save = false, isID = true)
    private long id;

    @IDatabaseField(columnName = "path")
    private String path;

    @IDatabaseField(columnName = "name")
    private String name;

    @IDatabaseField(columnName = "size")
    private int size;

    @IDatabaseField(columnName = "repositoryVersionId")
    private long repositoryVersionId;

    public String getPath() {
        return path;
    }

    @Setter
    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    @Setter
    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    @Setter
    public void setSize(int size) {
        this.size = size;
    }

    public long getId() {
        return id;
    }




    public RepositoryVersion repositoryVersionConcrete;
    @ManyToOne(columnName = "repositoryVersionConcrete",columnNameRefTable = "repositoryVersionId")
    public RepositoryVersion getRepositoryVersionConcrete() {
        return repositoryVersionConcrete;
    }

    @Setter
    public void setRepositoryVersion(RepositoryVersion r){
        this.repositoryVersionConcrete = r;
        this.repositoryVersionId = r.getId();
    }


}
