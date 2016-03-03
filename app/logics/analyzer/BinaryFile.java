package logics.analyzer;

import interfaces.Component;
import logics.models.json.RenderChild;
import logics.models.json.RenderComponent;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by bedux on 24/02/16.
 */
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
        return this.features;
    }

    @Override
    public <T> T applyFunction(Function< Component, T> function) {
        return function.apply(this);
    }


//    @Override
//    public RenderChild applyRenderer() {
//        Features f = this.getFeatures();
//        RenderChild rc =   new RenderChild(new float[3],new RenderComponent((float)f.getBoundingBox().getWidth(),(float)f.getBoundingBox().getHeight(),(float)f.getBoundingBox().getWidth(),f.getColor(), f.getSegment(),this.getFeatures().getPath(),null));
//        rc.setFeatures(features);
//        return rc;
//    }

    @Override
    public RenderChild getRenderJSON() {
        return new RenderChild(new float[]{features.getRendererLeft(),0,features.getRendererTop()},new RenderComponent(features,null));
    }
}
