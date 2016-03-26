package logics.models.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import interfaces.DataAttributes;
import play.db.ebean.Model;

import javax.persistence.*;

/**
 * Created by bedux on 04/03/16.
 */
@Entity
@Table(name="ComponentInfo")
public class ComponentInfo extends Model implements DataAttributes {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @JsonIgnore
    public Long id;

    @JsonIgnore
    public String parent;

    public String fileName;


    public void setParent(String parent) {
        this.parent = parent;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public int noMethod = 0;
    public int noPrivateMethod = 0;
    public int noPublicMethod = 0;
    public int noProtectedMethod = 0;
    public int noF = 0; //number of fields
    public int noForSTM =0; //number of for loop
    public int noForeachSTM =0; //number of for loop
    public int noWhile =0; //number of foreach loop
    public int noIf = 0;
    public int noWord =0; //number of foreach loop
    public int size = 0;
    public int noLine = 0;


    public int getNoMethod() {
        return noMethod;
    }

    public void setNoMethod(int noMethod) {
        this.noMethod = noMethod;
    }

    public int getNoPrivateMethod() {
        return noPrivateMethod;
    }

    public void setNoPrivateMethod(int noPrivateMethod) {
        this.noPrivateMethod = noPrivateMethod;
    }

    public int getNoPublicMethod() {
        return noPublicMethod;
    }

    public void setNoPublicMethod(int noPublicMethod) {
        this.noPublicMethod = noPublicMethod;
    }

    public int getNoProtectedMethod() {
        return noProtectedMethod;
    }

    public void setNoProtectedMethod(int noProtectedMethod) {
        this.noProtectedMethod = noProtectedMethod;
    }

    public int getNoF() {
        return noF;
    }

    public void setNoF(int noF) {
        this.noF = noF;
    }

    public int getNoForSTM() {
        return noForSTM;
    }

    public void setNoForSTM(int noForSTM) {
        this.noForSTM = noForSTM;
    }

    public int getNoForeachSTM() {
        return noForeachSTM;
    }

    public void setNoForeachSTM(int noForeachSTM) {
        this.noForeachSTM = noForeachSTM;
    }

    public int getNoWhile() {
        return noWhile;
    }

    public void setNoWhile(int noWhile) {
        this.noWhile = noWhile;
    }

    public int getNoIf() {
        return noIf;
    }

    public void setNoIf(int noIf) {
        this.noIf = noIf;
    }

    public int getNoWord() {
        return noWord;
    }

    public void setNoWord(int noWord) {
        this.noWord = noWord;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public int getNoLine() {
        return noLine;
    }

    @Override
    public void setNoLine(int noLine) {
        this.noLine = noLine;
    }

    @ManyToOne
    @JsonIgnore
    public RepositoryVersion repository;


    public static ComponentInfo createComponentInfo(RepositoryVersion repo,String fileName){
        ComponentInfo componentInfo = new ComponentInfo();
        componentInfo.repository = repo;
        componentInfo.fileName = fileName;
        componentInfo.save();
        return componentInfo;
    }

    public static Finder<Long,ComponentInfo> find = new Finder<Long, ComponentInfo>(Long.class,ComponentInfo.class);


}