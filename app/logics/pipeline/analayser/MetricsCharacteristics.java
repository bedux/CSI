package logics.pipeline.analayser;

import logics.models.db.RepositoryRender;
import logics.models.db.RepositoryVersion;
import logics.models.modelQuery.IQuery;

/**
 * Created by bedux on 13/04/16.
 */
public class MetricsCharacteristics {

private final     IQuery<String,Long> width ;
    private final IQuery<String,Long> height ;
    private final  IQuery<String,Long> color ;
    private final String metricType;

    public MetricsCharacteristics( IQuery<String,Long> width,  IQuery<String,Long> height,  IQuery<String,Long> color, String metricType) {
        this.width = width.clone();
        this.height = height.clone();
        this.color = color.clone();
        this.metricType = metricType;
    }

    public  IQuery<String,Long> getWidth() {
        return width;
    }

    public  IQuery<String,Long> getHeight() {
        return height;
    }

    public  IQuery<String,Long> getColor() {
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
