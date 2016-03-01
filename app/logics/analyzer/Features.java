package logics.analyzer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import logics.renderTools.Packageable;

import java.nio.file.Path;

@JsonIgnoreProperties
public class Features extends Packageable {

    private final String name;
    private final String path;
    private long wordCount =0;
    private int methodsNumber=0;
    private final Path filePath;
    private long size=0;

    public Features(String name, String path,Path filePath, int methodsNumber, long wordCount) {
        super();
        this.name = name;
        this.path = path;
        this.methodsNumber = methodsNumber;
        this.wordCount = wordCount;
        this.filePath=filePath;
        bindingToPakageble();

    }

    public Features(String name, String path,Path filePath, int wordCount) {
        super();

        this.name = name;
        this.path = path;
        this.wordCount = wordCount;
        this.filePath=filePath;
        bindingToPakageble();
    }

    public Features(String name, String path,Path filePath) {
        super();
        this.name = name;
        this.path = path;
        this.filePath=filePath;
        bindingToPakageble();
    }


    // this function for map the different information over the system
    protected void bindingToPakageble(){
        super.setWidth(wordCount);
        super.setDeep(wordCount);
        super.setHeight(5+size/128);
        super.setColor(new float[]{1,0,1});
        super.setSegment(4);
    }

    public long getWordCount() {
        return wordCount;
    }
    public int getMethodsNumber() {
        return methodsNumber;
    }
    public String getPath() {
        return path;
    }
    public String getName() {
        return name;
    }
    public long   getSize() {
        return size;
    }
    public Path getFilePath() {
        return filePath;
    }


    public void setSize(Long size) {
        this.size = size;
        bindingToPakageble();
    }
    public void setMethodsNumber(int methodsNumber) {
        this.methodsNumber = methodsNumber;
        bindingToPakageble();
    }
    public void setWordCount(long wordCount) {
        this.wordCount = wordCount;
        bindingToPakageble();
    }



    @Override
    public String toString(){
        return path + " WC: "+ wordCount;
    }
}
