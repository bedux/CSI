package logics.analyzer;

import interfaces.DataAttributes;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bedux on 17/03/16.
 */
public class DataFeatures extends Features implements DataAttributes {

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


    public static Map<String, String> getMapMethod = new HashMap() {
        {
            put("Number of Fileds (Width)  ", DataName.NOF.getValue());
            put("Number of Methods (Height)", DataName.NoMethod.getValue());
            put("Number of Lines (Color)", DataName.NoLine.getValue());


        }
    };


    public DataFeatures(String name, String path, Path filePath) {
        super(name, path, filePath);


    }

    @Override
    public void bindingToPakageble() {

        super.setDeep(getNoF());
        super.setWidth(getNoF());
        super.setHeight(getNoMethod());
        super.setColor(getNoLine());
        super.setBuildingType(1);

    }
}

