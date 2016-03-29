package logics.models.db;


import exception.CustomException;
import interfaces.VersionedSystem;
import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;
import logics.versionUtils.GitRepo;

@IDatabaseClass(tableName = "Repository")
public class Repository {

    @IDatabaseField(columnName = "id", save = false, isID = true)
    public int id;

    @IDatabaseField(columnName = "url")
    public String url;

    @IDatabaseField(columnName = "usr")
    public String usr;

    @IDatabaseField(columnName = "pwd")
    public String pwd;

    @IDatabaseField(columnName = "subversionType")
    public String subversionType;

    public VersionedSystem CreateSystem() {

        if (this.subversionType.equalsIgnoreCase("GIT")) {
            return new GitRepo(this);
        } else {
            throw new CustomException("No Compatible Subversion System Found");
        }

    }


}
