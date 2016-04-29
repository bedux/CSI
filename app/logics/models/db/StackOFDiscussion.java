package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;
import logics.databaseUtilities.OneToMany;

import java.util.List;

/**
 * Created by bedux on 11/04/16.
 */
@IDatabaseClass(tableName = "discussion")
public class StackOFDiscussion extends BaseTable{



    @IDatabaseField(columnName = "id", save = false, isID = true)
    public long id;


    @IDatabaseField(columnName = "url")
    public String discussionURL;

    public long getId() {
        return id;
    }

    List<ImportDiscussion> listOfImportDiscussion;
    @OneToMany(referTo = {"listOfImportDiscussion"} )
    public  List<ImportDiscussion> getListOfImportDiscussion() {
        return listOfImportDiscussion;
    }

    public String getDiscussionURL() {
        return discussionURL;
    }

    public void setDiscussionURL(String discussionURL) {
        this.discussionURL = discussionURL;
    }
}


