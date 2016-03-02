package logics.analyzer;

import interfaces.Component;
import logics.models.json.RenderChild;
import logics.models.json.RenderComponent;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by bedux on 24/02/16.
 */
public class DataFile implements Component{

    public List<Component> getComponentList(){
        return new ArrayList<>();
    }

    private Features features;
    public DataFile(Features current){
        features = current;
    }



    public boolean add(String s,Path f,String allPath) {
        return false;
    }



    @Override
    public void applyIndependent(Consumer<Component> function){
        function.accept(this);
    }

    @Override
    public RenderChild applyRenderer() {
        return new RenderChild(new float[]{features.getRendererLeft(),0,features.getRendererTop()},new RenderComponent(this.getFeatures().getRendererWidth(),this.getFeatures().getHeight(),this.getFeatures().getRendererDeep(),new float[]{(float)Math.random(),(float)Math.random(),(float)Math.random()},4,this.getFeatures().getPath(),null));
    }

    @Override
    public Features getFeatures() {
        return this.features;
    }



}
