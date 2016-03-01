package interfaces;

import logics.analyzer.Features;
import logics.models.json.RenderChild;
import logics.models.json.RenderComponent;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by bedux on 24/02/16.
 */
public interface Component {

    boolean add(String s,Path f,String allPath);

    Features getFeatures();
    //Compute the statistics independently from level to level
    void applyIndependent(Consumer<Component> function);

     RenderChild applyRenderer() ;

     List<Component> getComponentList();



}
