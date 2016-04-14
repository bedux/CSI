package logics.pipeline.analayser;

import interfaces.HandlerParam;
import logics.models.db.RepositoryRender;
import logics.models.db.RepositoryVersion;
import logics.models.query.ComputeWithSingleQuery;
import logics.models.query.CountingExternalPackage;
import logics.models.query.IComputeAttributeContainer;
import logics.models.query.QueryList;
import logics.pipeline.storing.StoreHandlerResult;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bedux on 08/03/16.
 */
public class AnalyserHandlerParam implements HandlerParam {
    public RepositoryVersion repositoryVersion;



    List<MetricsCharatteristics> metricsToCompute = new ArrayList<>();

    public AnalyserHandlerParam(@NotNull RepositoryVersion repositoryVersion) {
        this.repositoryVersion = repositoryVersion;

    }

    public AnalyserHandlerParam(@NotNull StoreHandlerResult storeHandlerResult) {
        this.repositoryVersion = storeHandlerResult.repositoryVersion;
      

    }

    public  void addMetrics(MetricsCharatteristics metricsCharatteristics){
        metricsToCompute.add(metricsCharatteristics);
    }

    public List<MetricsCharatteristics> listMetricsToCompute(){
        return metricsToCompute;
    }

}
