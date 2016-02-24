package logics.analyzer;

import interfaces.Component;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bedux on 24/02/16.
 */
public class BinaryFile implements Component {


    private Features features;
    public BinaryFile(Features current){
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
