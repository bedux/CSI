package logics.models.db;

import logics.databaseUtilities.*;

import java.util.List;

@IDatabaseClass(tableName = "RepositoryVersion")
public class RepositoryVersion extends BaseTable{
    public long getId() {
        return id;
    }

    public String getLocalPath() {
        return localPath;
    }

    @IDatabaseField(columnName = "id", save = false, isID = true)
    private long id;

    @IDatabaseField(columnName = "localPath")
    private String localPath;

    @IDatabaseField(columnName = "repositoryId")
    private long repositoryId;



    private Repository repositoryConcrete;
    @ManyToOne(columnName = "repositoryConcrete",columnNameRefTable = "repositoryId")
    public Repository getRepository() {
        return repositoryConcrete;
    }



    private List<RepositoryRender> listOfRepositoryRender = null;
    @OneToMany(referTo = {"listOfRepositoryRender"} )
    public List<RepositoryRender> getListOfRepositoryRender() {
        return listOfRepositoryRender;
    }


    private List<File> listOfFile = null;
    @OneToMany(referTo = {"listOfFile"} )
    public List<File> getListOfFile() {
        return listOfFile;
    }


    @Setter
    public void addFile(File file){
        listOfFile.add(file);
        file.setRepositoryVersion(this);

    }

    @Setter
    public void addRepositoryRender(RepositoryRender repositoryRender){
        listOfRepositoryRender.add(repositoryRender);
        repositoryRender.setRepositoryVersionConcrete(this);

    }

    @Setter
    public void setRepositoryConcrete(Repository r){
        this.repositoryId = r.getId();
        this.repositoryConcrete = r;
    }

}
