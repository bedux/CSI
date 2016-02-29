package logics.analyzer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import logics.renderTools.Packageable;

import java.nio.file.Path;

@JsonIgnoreProperties
public class Features extends Packageable {

    private final String name;
    private final String path;
    private long worldCount=0;
    private int methodsNumber=0;
    private final Path filePath;
    private long size=0;

    public Features(String name, String path,Path filePath, int methodsNumber, long worldCount) {
        this.name = name;
        this.path = path;
        this.methodsNumber = methodsNumber;
        this.worldCount = worldCount;
        this.filePath=filePath;
        bindingToPakageble();

    }

    public Features(String name, String path,Path filePath, int worldCount) {

        this.name = name;
        this.path = path;
        this.worldCount = worldCount;
        this.filePath=filePath;
        bindingToPakageble();
    }

    public Features(String name, String path,Path filePath) {

        this.name = name;
        this.path = path;
        this.filePath=filePath;
        bindingToPakageble();
    }


    //Modifie this function for map the different information over the sytem
    private void bindingToPakageble(){
        super.setWidth(size/100);
        super.setDeep(size/100);
        super.setHeight(worldCount/10);
        super.setColor(new float[]{1,0,1});
        super.setSegment(4);

    }

    public long getWorldCount() {
        return worldCount;
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
        this.size = size; bindingToPakageble();
    }
    public void setMethodsNumber(int methodsNumber) {
        this.methodsNumber = methodsNumber;
        bindingToPakageble();
    }
    public void setWorldCount(long worldCount) {
        this.worldCount = worldCount;
        bindingToPakageble();
    }



    @Override
    public String toString(){
        return path + " WC: "+ worldCount;
    }
}
