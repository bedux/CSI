package logics.analyzer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import logics.renderTools.Packageable;

import java.nio.file.Path;

@JsonIgnoreProperties
public  class Features extends Packageable{

    @JsonIgnore private final String name;
    @JsonIgnore private final String path;
    @JsonIgnore private final Path filePath;
    @JsonIgnore private int remoteness = 0;


    public void setRemoteness(int remoteness){
        this.remoteness = remoteness;
    }

    public int getRemoteness(){
       return this.remoteness;
    }

    public Features(String name, String path,Path filePath) {
        super();
        this.name = name;
        this.path = path;
        this.filePath=filePath;
    }


    public Path getFilePath() {
        return filePath;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    @Override
    protected void bindingToPakageble(){

    }

    @Override
    public String toString(){
        return path ;
    }
}
