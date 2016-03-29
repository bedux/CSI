package logics.models.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import logics.analyzer.Features;

import java.util.Arrays;

/**
 * Created by bedux on 26/02/16.
 */
public class RenderChild {
    public float[] position;
    public RenderComponent data;

    @JsonIgnore
    private Features f;

    public RenderChild(float[] position, RenderComponent data) {
        this.position = position;
        this.data = data;
    }

    @JsonIgnore

    public Features getFeatures() {
        return f;
    }

    @JsonIgnore
    public void setFeatures(Features f) {
        this.f = f;
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
