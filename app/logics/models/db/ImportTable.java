package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;
import logics.databaseUtilities.ManyToOne;
import logics.databaseUtilities.OneToMany;
import logics.models.db.information.MethodInfoJSON;

import java.util.List;

/**
 * Created by bedux on 10/04/16.
 */
@IDatabaseClass(tableName = "import")
public class ImportTable extends BaseTable {

    public long getId() {
        return id;
    }

    public String getPackageDiscussion() {
        return packageDiscussion;
    }

    public void setPackageDiscussion(String packageDiscussion) {
        this.packageDiscussion = packageDiscussion;
    }

    List<ImportDiscussion> listOfImportDiscussion;
    @OneToMany(referTo = {"listOfImportDiscussion"} )
    public  List<ImportDiscussion> getListOfImportDiscussion() {
        return listOfImportDiscussion;
    }

    @IDatabaseField(columnName = "id", save = false, isID = true)
    public long id;


    @IDatabaseField(columnName = "package")
    public String packageDiscussion;




}
