package logics.analyzer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import logics.renderTools.Packageable;

import java.nio.file.Path;

@JsonIgnoreProperties
public abstract class Features extends Packageable{

    @JsonIgnore private final String name;
    @JsonIgnore private final String path;
    @JsonIgnore private final Path filePath;
    @JsonIgnore private int remoteness = 0;


    public void setRemoteness(int remoteness){
        this.remoteness = remoteness;
    }
    public int getRemotness(){
       return this.remoteness;
    }



    public Features(String name, String path,Path filePath, int wordCount) {
        super();

        this.name = name;
        this.path = path;
        this.filePath=filePath;
    }

    public Features(String name, String path,Path filePath) {
        super();
        this.name = name;
        this.path = path;
        this.filePath=filePath;
    }


    // this function for map the different information over the system
//    protected void bindingToPakageble(){
//
//        super.setWidth(getWordCount());
//        super.setDeep(getWordCount());
//        super.setHeight(getMethodsNumber());
//        super.setColor(new float[]{1,0,1});
//        super.setSegment(4);
//    }
    public Path getFilePath() {
        return filePath;
    }
    public String getPath() {
        return path;
    }
    public String getName() {
        return name;
    }


//    public long getWordCount() {
//        return wordCount;
//    }
//    public int  getMethodsNumber() {
//        return methodsNumber;
//    }
//    public long getSize() {
//        return size;
//    }
//
//
//    public void setSize(Long size) {
//        this.size = size;
//        bindingToPakageble();
//    }
//    public void setMethodsNumber(int methodsNumber) {
//        this.methodsNumber = methodsNumber;
//        bindingToPakageble();
//    }
//    public void setWordCount(long wordCount) {
//        this.wordCount = wordCount;
//        bindingToPakageble();
//    }


    @Override
    protected abstract void bindingToPakageble();



    @Override
    public String toString(){
        return path ;
    }
}
