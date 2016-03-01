package logics.analyzer;

import interfaces.Component;
import logics.models.json.RenderChild;
import logics.models.json.RenderComponent;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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

//    @Override
//    public RenderChild applyRenderer() {
//        Features f = this.getFeatures();
//        RenderChild rc =   new RenderChild(new float[3],new RenderComponent((float)f.getBoundingBox().getWidth(),(float)f.getBoundingBox().getHeight(),(float)f.getBoundingBox().getWidth(),f.getColor(), f.getSegment(),this.getFeatures().getPath(),null));
//        rc.setFeatures(features);
//        return rc;
//    }

    @Override
    public RenderChild applyRenderer() {
        return new RenderChild(new float[]{features.getBoundingBox().getTop(),0,features.getBoundingBox().getLeft()},new RenderComponent(this.getFeatures().getWidth(),this.getFeatures().getHeight(),this.getFeatures().getDeep(),new float[]{(float)Math.random(),(float)Math.random(),(float)Math.random()},4,this.getFeatures().getPath(),null));
    }
}
