package logics;

import java.nio.file.Path;

/**
 * Provide method to working with the file extensions
 */
public class ExtensionTool {

    private final  String path;
    private final  String currentExtension;
    private final Path filePath;
    /***
     *
     * @param filePath The file path
     */
    public ExtensionTool(Path filePath){
        this.path = filePath.toString();
        this.filePath = filePath;
        this.currentExtension = path.substring(path.lastIndexOf(".") + 1);
    }

    /***
     *
     * @param filePath The file path
     */
    public ExtensionTool(Path filePath,String path){
        this.path = path;
        this.filePath = filePath;
        this.currentExtension = path.substring(path.lastIndexOf(".") + 1);
    }

    /***
     *
     * @return the extension of the file
     */
    public String getExtension(){
            return currentExtension;
    }

    /***
     * @return is a java file?
     */
    public Boolean isJava(){
        return currentExtension.indexOf("java") == 0;
    }

    /***
     *
     * @return the path string
     */
    public String getPath(){
        return path;
    }


    public Path getFilePath(){
        return filePath;
    }


}
