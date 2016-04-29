package logics.models.db;

import logics.databaseUtilities.IDatabaseClass;
import logics.databaseUtilities.IDatabaseField;
import logics.databaseUtilities.OneToMany;
import logics.databaseUtilities.Setter;
import logics.models.db.information.JavaFileInformation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@IDatabaseClass(tableName = "JavaFile")
public class JavaFile extends File {



    @IDatabaseField(columnName = "id", save = false, isID = true)
    private long id;



    @IDatabaseField(columnName = "information", fromJSON = true)
    private JavaFileInformation json;





    @Setter
    public void setJavaFileInformation(JavaFileInformation info){
        this.json = info;
    }


    @Setter
    public void setJson(JavaFileInformation json) {
        this.json = json;
    }

    public JavaFileInformation getJson() {
        return json;
    }





    public long getId() {
        return id;
    }






    List<JavaSourceObject> listOfObject = null;
    @OneToMany(referTo = {"listOfObject"} )
    public List<JavaSourceObject> getListOfObject() {
        return listOfObject;
    }

    @OneToMany(referTo = {"listOfJavaClass"} )
    public List<JavaClass> getListOfJavaClass() {
        return listOfJavaClass;
    }

    @OneToMany(referTo = {"listOfJavaInterface"} )
    public List<JavaInterface> getListOfJavaInterface() {
        return listOfJavaInterface;
    }

    @OneToMany(referTo = {"listOfJavaEnum"} )
    public List<JavaEnum> getListOfJavaEnum() {
        return listOfJavaEnum;
    }


    @OneToMany(referTo = {"listOfJavaImport"} )
    public List<JavaImport> getListOfJavaImport() {
        return listOfJavaImport;
    }

    @OneToMany(referTo = {"listOfJavaPackage"} )
    public List<JavaPackage> getListOfJavaPackage() {
        return listOfJavaPackage;
    }


    public void addlistOfJavaClass(JavaClass jf){
        this.getListOfJavaClass().add(jf);
        jf.setJavaFile(this);
    }


    public void addListOfJavaInterface(JavaInterface jf){
        this.getListOfJavaInterface().add(jf);
        jf.setJavaFile(this);

    }

    public void addlistOfJavaPackage(JavaPackage jf){
        this.getListOfJavaPackage().add(jf);
        jf.setJavaFile(this);

    }

    public void addlistOfJavaEnum(JavaEnum jf){
        this.getListOfJavaEnum().add(jf);
        jf.setJavaFile(this);

    }

    public void addlistOfJavaImport(JavaImport jf){
        this.getListOfJavaImport().add(jf);
        jf.setJavaFile(this);

    }

    List<JavaClass> listOfJavaClass = null;

    List<JavaInterface> listOfJavaInterface = null ;

    List<JavaEnum> listOfJavaEnum = null;

    List<JavaImport> listOfJavaImport = null;

    List<JavaPackage> listOfJavaPackage = null;




}
