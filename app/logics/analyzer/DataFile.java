package logics.analyzer;

import interfaces.Component;
import logics.models.json.RenderChild;
import logics.models.json.RenderComponent;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

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
    public void applyIndependentArray(Consumer<List<Component>> function){
    }


    @Override
    public void applyIndependent(Consumer<Component> function){
        function.accept(this);
    }

    @Override
    public Features getFeatures() {
        return this.features;
    }

    @Override
    public RenderChild applyRenderer(Consumer<Component> function) {
        Features f = this.getFeatures();
        RenderChild rc =   new RenderChild(new float[3],new RenderComponent((float)f.getBoundingBox().getWidth(),(float)f.getBoundingBox().getHeight(),(float)f.getBoundingBox().getWidth(),f.getColor(), f.getSegment(),this.getFeatures().getPath(),null));
        rc.setFeatures(features);
        return rc;
    }

}
