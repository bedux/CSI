package logics.models.db;


import exception.CustomException;
import interfaces.VersionedSystem;
import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;
import logics.databaseUtilities.OneToMany;
import logics.databaseUtilities.Setter;
import logics.versionUtils.GitRepo;

import java.util.ArrayList;
import java.util.List;

@IDatabaseClass(tableName = "repository")
public class Repository {

    @IDatabaseField(columnName = "id", save = false, isID = true)
    private long id;

    @IDatabaseField(columnName = "url")
    private String url="";

    @IDatabaseField(columnName = "usr")
    private String usr="";

    @IDatabaseField(columnName = "pwd")
    private String pwd="";

    @IDatabaseField(columnName = "subversionType")
    private String subversionType="";

    public long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getUsr() {
        return usr;
    }

    public String getPwd() {
        return pwd;
    }

    public String getSubversionType() {
        return subversionType;
    }


    public List<RepositoryVersion> listOfRepositoryVersion = null;
    @OneToMany(referTo = {"listOfRepositoryVersion"} )
    public List<RepositoryVersion> getListOfRepositoryVersion() {
        return listOfRepositoryVersion;
    }


    public void addlistOfRepositoryVersion(RepositoryVersion jf){
        this.getListOfRepositoryVersion();
        if(this.getListOfRepositoryVersion()==null){
            this.listOfRepositoryVersion= new ArrayList<>();
        }
        listOfRepositoryVersion.add(jf);
        jf.setRepositoryConcrete(this);

    }

@Setter
    public void setUrl(String url) {
        this.url = url;
    }
    @Setter

    public void setUsr(String usr) {
        this.usr = usr;
    }
    @Setter

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    @Setter

    public void setSubversionType(String subversionType) {
        this.subversionType = subversionType;
    }
}
