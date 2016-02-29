package logics.models.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import interfaces.Component;
import logics.analyzer.Features;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * Created by bedux on 26/02/16.
 */
public class RenderChild {
    public float[] position;
    public RenderComponent data;

    @JsonIgnore
    private Features f;

    @JsonIgnore

    public Features getFeatures() {
        return f;
    }

    @JsonIgnore
    public void setFeatures(Features f) {
        this.f = f;
    }

    public RenderChild(float[] position, RenderComponent data) {
        this.position = position;
        this.data = data;
    }

    @Override
    public String toString() {
        return "RenderChild{" +
                "position=" + Arrays.toString(position) +
                ", data=" + data +
                ", f=" + f +
                '}';
    }
}
