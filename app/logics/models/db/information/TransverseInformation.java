package logics.models.db.information;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;
import logics.databaseUtilities.ManyToOne;
import logics.databaseUtilities.Setter;
import logics.models.db.JavaSourceObject;

/**
 * Created by bedux on 25/03/16.
 */
@IDatabaseClass(tableName = "TransverseInformation",idName = "id")
public class TransverseInformation {

    @IDatabaseField(columnName = "id", save = false, isID = true)
    public long id;

    @IDatabaseField(columnName = "ContainsTransverseInformation")
    public long containsTransverseInformation;


    public long getContainsTransverseInformation() {
        return containsTransverseInformation;
    }

    @Setter
    public void setContainsTransverseInformation(long containsTransverseInformation) {
        this.containsTransverseInformation = containsTransverseInformation;
    }

    public long getId() {
        return id;
    }


}
