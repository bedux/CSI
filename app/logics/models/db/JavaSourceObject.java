package logics.models.db;

import exception.CustomException;
import logics.databaseUtilities.*;

import java.util.ArrayList;
import java.util.List;


@IDatabaseClass(tableName = "JavaSourceObject")
public class JavaSourceObject extends BaseTable{

    public long getId() {
        return id;
    }

    @IDatabaseField(columnName = "id", save = false, isID = true)
    private long id;


    @IDatabaseField(columnName = "javafile")
    private long javaFile;


    private JavaFile javaFileConcrete;
    @ManyToOne(columnName = "javaFileConcrete",columnNameRefTable = "javafile")
    public JavaFile getJavaFileConcrete() {
        return javaFileConcrete;
    }


    @Setter
    public void setJavaFile(JavaFile jf){
        javaFile = jf.getId();
        javaFileConcrete = jf;
    }


    List<JavaMethod> listOfMethod = null;

    @OneToMany(referTo = {"listOfMethod"} )
    public  List<JavaMethod> getListOfMethod() {
//        if(listOfMethod==null){
//            throw new CustomException("Error: Return null on getListOfMethod!");
//        }
        return listOfMethod;
    }

    List<JavaField> listOfField = null;

    @OneToMany(referTo = {"listOfField"} )
    public  List<JavaField> getListOfField() {
        return listOfField;
    }


    public void addListOfField(JavaField jf){
        if(  this.getListOfField()==null){
            this.listOfField= new ArrayList<>();
        }
        this.getListOfField().add(jf);
        jf.setJavaSource(this);
    }


    public void addlistOfMethod(JavaMethod jf){
        if(  this.getListOfMethod()==null){
            this.listOfMethod= new ArrayList<>();
        }
        this.getListOfMethod().add(jf);
        jf.setJavaSource(this);

    }



}
