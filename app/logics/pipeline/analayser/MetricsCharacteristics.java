package logics.pipeline.analayser;

import logics.models.db.RepositoryRender;
import logics.models.db.RepositoryVersion;
import logics.models.modelQuery.IQuery;

import java.util.function.Function;

/**
 * Created by bedux on 13/04/16.
 */
public class MetricsCharacteristics {

private final Function<String,Long> width ;
    private final Function<String,Long> height ;
    private final  Function<String,Long> color ;
    private final String metricType;

    public MetricsCharacteristics( Function<String,Long> width,  Function<String,Long> height,  Function<String,Long> color, String metricType) {
        this.width = width;
        this.height = height;
        this.color = color;
        this.metricType = metricType;
    }

    public  Function<String,Long> getWidth() {
        return width;
    }

    public  Function<String,Long> getHeight() {
        return height;
    }

    public  Function<String,Long> getColor() {
        return color;
    }

    public String getMetricType() {
        return metricType;
    }

    public RepositoryRender repositoryRender(RepositoryVersion repositoryVersion){
        RepositoryRender repositoryRender = new RepositoryRender();
        repositoryRender.setRepositoryVersionConcrete(repositoryVersion);
        repositoryRender.setrepositoryConcrete(repositoryVersion.getRepository());
        repositoryRender.setMetricType( metricType);
        return  repositoryRender;
    }
}
