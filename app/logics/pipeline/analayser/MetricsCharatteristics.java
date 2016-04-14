package logics.pipeline.analayser;

import logics.models.db.RepositoryRender;
import logics.models.db.RepositoryVersion;
import logics.models.query.ComputeWithSingleQuery;
import logics.models.query.CountingExternalPackage;
import logics.models.query.IComputeAttributeContainer;
import logics.models.query.QueryList;

/**
 * Created by bedux on 13/04/16.
 */
public class MetricsCharatteristics {

private final    IComputeAttributeContainer width ;
    private final    IComputeAttributeContainer height ;
    private final IComputeAttributeContainer color ;
    private final String metricType;

    public MetricsCharatteristics(IComputeAttributeContainer width, IComputeAttributeContainer height, IComputeAttributeContainer color,String metricType) {
        this.width = width;
        this.height = height;
        this.color = color;
        this.metricType = metricType;
    }

    public IComputeAttributeContainer getWidth() {
        return width;
    }

    public IComputeAttributeContainer getHeight() {
        return height;
    }

    public IComputeAttributeContainer getColor() {
        return color;
    }

    public String getMetricType() {
        return metricType;
    }

    public RepositoryRender repositoryRender(RepositoryVersion repositoryVersion){
        RepositoryRender repositoryRender = new RepositoryRender();
        repositoryRender.repositoryId = repositoryVersion.repositoryId;
        repositoryRender.repositoryVersion = repositoryVersion.id;
        repositoryRender.metricType = metricType;
        return  repositoryRender;
    }
}
