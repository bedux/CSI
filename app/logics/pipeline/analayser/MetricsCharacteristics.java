package logics.pipeline.analayser;

import logics.models.newDatabase.RepositoryRender;
import logics.models.newDatabase.RepositoryVersion;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by bedux on 13/04/16.
 */
public class MetricsCharacteristics {

private final Supplier<Function<String,Long>> width ;
    private final Supplier<Function<String,Long>> height ;
    private final  Supplier<Function<String,Long>> color ;
    private final String metricType;

    public MetricsCharacteristics(Supplier<Function<String, Long>> width, Supplier<Function<String,Long>> height, Supplier<Function<String,Long>> color, String metricType) {
        this.width = width;
        this.height = height;
        this.color = color;
        this.metricType = metricType;
    }

    public  Function<String,Long> getWidth() {
        return width.get();
    }

    public  Function<String,Long> getHeight() {
        return height.get();
    }

    public  Function<String,Long> getColor() {
        return color.get();
    }

    public String getMetricType() {
        return metricType;
    }

    public RepositoryRender repositoryRender(RepositoryVersion repositoryVersion){
        RepositoryRender repositoryRender = new RepositoryRender();
        //repositoryRender.setRepositoryVersionConcrete(repositoryVersion);
        repositoryRender.repositoryversion = repositoryVersion;
        repositoryRender.repository = repositoryVersion.repository;
        repositoryRender.metrictype = ( metricType);
        repositoryRender.save();
        return  repositoryRender;
    }
}
