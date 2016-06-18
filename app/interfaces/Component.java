package interfaces;

import logics.analyzer.Features;
import logics.models.json.RenderChild;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;

/**
 * Created by bedux on 24/02/16.
 */
public interface Component {

    boolean add(String s, Path f, String allPath);

    Features getFeatures();

    //Compute the statistics independently from level to level
    <T> T applyFunction(Function<Component, T> function);

    RenderChild getRenderJSON();

    List<Component> getComponentList();

    default float safeNumber(float number){
        return (Float.isNaN(number))?0:number;

    }

}
