package logics.analyzer;

import interfaces.Component;
import interfaces.FileComponent;
import logics.models.json.RenderChild;
import logics.models.json.RenderComponent;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


public class BinaryFile implements Component {

    public List<Component> getComponentList(){
        return new ArrayList<>();
    }

    private Features features;

    public BinaryFile(Features current){
        features = current;
    }

    public boolean add(String s,Path f,String allPath) {
        return false;
    }

    @Override
    public Features getFeatures() {
        return features;
    }

    @Override
    public <T> T applyFunction(Function< Component, T> function) {
        return function.apply(this);
    }

    @Override
    public RenderChild getRenderJSON() {
        return new RenderChild(new float[]{features.getRendererLeft(),0,features.getRendererTop()},new RenderComponent(features,null));
    }
}
