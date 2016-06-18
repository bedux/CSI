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
public class DataFile implements Component {

    private Features features;

    public DataFile(Features current) {
        features = current;
    }

    public List<Component> getComponentList() {
        return new ArrayList<>();
    }

    /**
     * Add a new node. Well in  DataFile does nothing
     * @param s
     * @param f
     * @param allPath
     * @return
     */
    public boolean add(String s, Path f, String allPath) {
        return false;
    }

    /**
     * Generate the renderer json for the current node
     * @return
     */
    @Override
    public RenderChild getRenderJSON() {

        features.setColorMetrics(safeNumber(features.getColorMetrics()));
        features.setColor(safeNumber(features.getColor()));
        return new RenderChild(new float[]{features.getRendererLeft(), 0, features.getRendererTop()}, new RenderComponent(features, null));
    }

    @Override
    public Features getFeatures() {
        return this.features;
    }

    /***
     * Apply a function on the current node.
     * @param function
     * @param <T>
     * @return
     */
    @Override
    public <T> T applyFunction(Function<Component, T> function) {
        return function.apply(this);
    }


}
