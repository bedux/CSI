package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;
import logics.databaseUtilities.ManyToOne;
import logics.databaseUtilities.Setter;


@IDatabaseClass(tableName = "JavaSpecificComponent",idName = "id")
public class JavaSpecificComponent  extends BaseTable{

    @IDatabaseField(columnName = "id", save = false, isID = true)
    private long id;

    @IDatabaseField(columnName = "javaSource")
    private long javaSource;

    private JavaSourceObject javaClassConcrete;
    @ManyToOne(columnName = "javaClassConcrete",columnNameRefTable = "javaSource")
    public JavaSourceObject getJavaClassConcrete() {
        return javaClassConcrete;
    }




    @Setter
    public void setJavaSource(JavaSourceObject js){
        this.javaSource = js.getId();
        this.javaClassConcrete = js;

    }

}
