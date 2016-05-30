package logics.analyzer;

import interfaces.Component;
import logics.models.json.RenderChild;
import logics.models.json.RenderComponent;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


public class BinaryFile implements Component {

    private Features features;


    public BinaryFile(Features current) {
        features = current;
    }

    public List<Component> getComponentList() {
        return new ArrayList<>();
    }

    /**
     * Add a new node. Well in  binary does nothing
     * @param s
     * @param f
     * @param allPath
     * @return
     */
    public boolean add(String s, Path f, String allPath) {
        return false;
    }

    @Override
    public Features getFeatures() {
        return features;
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

    /**
     * Generate the renderer json for the current node
     * @return
     */
    @Override
    public RenderChild getRenderJSON() {
        return new RenderChild(new float[]{features.getRendererLeft(), 0, features.getRendererTop()}, new RenderComponent(features, null));
    }
}
