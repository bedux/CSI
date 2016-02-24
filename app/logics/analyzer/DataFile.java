package logics.analyzer;

import interfaces.Component;

import java.nio.file.Path;

/**
 * Created by bedux on 24/02/16.
 */
public class DataFile implements Component{


    private Features features;
    public DataFile(Features current){
        features = current;
    }

    @Override
    public Features operation() {
        System.out.println(features.getPath());
        return features;
    }

    @Override
    public Features operation(Features features) {
        return null;
    }

    public boolean add(String s,Path f,String allPath) {
        return false;

    }

}
