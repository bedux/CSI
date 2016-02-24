package logics.analyzer;

/**
 * Created by bedux on 24/02/16.
 */
public class Features {

    private final String name;
    private final String path;

    private int worldCount;
    private int methodsNumber;

    public Features(String name, String path, int methodsNumber, int worldCount) {
        this.name = name;
        this.path = path;
        this.methodsNumber = methodsNumber;
        this.worldCount = worldCount;
    }

    public Features(String name, String path, int worldCount) {

        this.name = name;
        this.path = path;
        this.worldCount = worldCount;
    }

    public Features(String name, String path) {

        this.name = name;
        this.path = path;
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
}
