package logics.analyzer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import logics.renderTools.Packageable;

import java.nio.file.Path;

@JsonIgnoreProperties
public class Features extends Packageable {

    @JsonIgnore
    private final String name;
    @JsonIgnore
    private final String path;
    @JsonIgnore
    private final Path filePath;
    @JsonIgnore
    private int remoteness = 0;

    private float widthMetrics = 0;
    private float heightMetrics = 0;
    private float depthMetrics = 0;

    public float getColorMetrics() {
        return colorMetrics;
    }

    public void setColorMetrics(float colorMetrics) {
        this.colorMetrics = colorMetrics;
    }

    private float colorMetrics = 0;


    public float getWidthMetrics() {
        return widthMetrics;
    }

    public void setWidthMetrics(float widthMetrics) {
        this.widthMetrics = widthMetrics;
    }

    public float getHeightMetrics() {
        return heightMetrics;
    }

    public void setHeightMetrics(float heightMetrics) {
        this.heightMetrics = heightMetrics;
    }

    public float getDepthMetrics() {
        return depthMetrics;
    }

    public void setDepthMetrics(float depthMetrics) {
        this.depthMetrics = depthMetrics;
    }

    public Features(String name, String path, Path filePath) {
        super();
        this.name = name;
        this.path = path;
        this.filePath = filePath;
    }

    public int getRemoteness() {
        return this.remoteness;
    }

    public void setRemoteness(int remoteness) {
        this.remoteness = remoteness;
    }

    public Path getFilePath() {
        return filePath;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    @Override
    protected void bindingToPakageble() {

    }

    @Override
    public String toString() {
        return path;
    }
}
