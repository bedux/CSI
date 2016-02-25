package logics.analyzer;

import java.nio.file.Path;

/**
 * Created by bedux on 24/02/16.
 */
public class Features {

    private final String name;
    private final String path;

    private final Path filePath;

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    private long size;

    public Path getFilePath() {
        return filePath;
    }

    private int worldCount;
    private int methodsNumber;

    public Features(String name, String path,Path filePath, int methodsNumber, int worldCount) {
        this.name = name;
        this.path = path;
        this.methodsNumber = methodsNumber;
        this.worldCount = worldCount;
        this.filePath=filePath;

    }

    public Features(String name, String path,Path filePath, int worldCount) {

        this.name = name;
        this.path = path;
        this.worldCount = worldCount;
        this.filePath=filePath;
    }

    public Features(String name, String path,Path filePath) {

        this.name = name;
        this.path = path;
        this.filePath=filePath;
    }

    public int getWorldCount() {
        return worldCount;
    }

    public void setWorldCount(int worldCount) {
        this.worldCount = worldCount;
    }

    public int getMethodsNumber() {
        return methodsNumber;
    }

    public void setMethodsNumber(int methodsNumber) {
        this.methodsNumber = methodsNumber;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }



    @Override
    public String toString(){
        return path + " WC: "+ worldCount;
    }
}
