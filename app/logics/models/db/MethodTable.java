package logics.models.db;


import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;
import logics.databaseUtilities.OneToMany;

import java.util.List;

/**
 * Created by bedux on 14/04/16.
 */
@IDatabaseClass(tableName = "method")
public class MethodTable extends BaseTable{

    public long getId() {
        return id;
    }



    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public int getnOfParam() {
        return nOfParam;
    }

    public void setnOfParam(int nOfParam) {
        this.nOfParam = nOfParam;
    }

    @IDatabaseField(columnName = "id", save = false, isID = true)
    private long id;


    @IDatabaseField(columnName = "methodname")
    private String methodName;

    @IDatabaseField(columnName = "params")
    private int nOfParam;


    List<MethodDiscussion> listOfMethodDiscussion;
    @OneToMany(referTo = {"listOfMethodDiscussion"} )
    public  List<MethodDiscussion> getListOfMethodDiscussion() {
        return listOfMethodDiscussion;
    }
}

