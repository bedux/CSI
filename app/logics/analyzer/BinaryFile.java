package logics.analyzer;

import interfaces.Component;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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
        return features;
    }

    @Override
    public Features operation(Features features) {
        return null;
    }
    public boolean add(String s,Path f,String allPath) {
        return false;
    }

    @Override
    public Features getFeatures() {
        return this.features;
    }

    @Override
    public void applyIndependent(Consumer<Component> function){
        function.accept(this);
    }

}
