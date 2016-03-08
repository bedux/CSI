package logics.pipeline;

import interfaces.Handler;
import logics.models.db.RepositoryVersion;
import logics.models.form.RepoForm;
import logics.pipeline.analayser.AnaliserHandler;
import logics.pipeline.analayser.AnalyserHandlerParam;
import logics.pipeline.clone.CloneHandler;
import logics.pipeline.clone.CloneHandlerParam;
import logics.pipeline.storing.StoreHandler;
import logics.pipeline.storing.StoreHandlerParam;

/**
 * Created by bedux on 07/03/16.
 */
public class PipelineManager {
    Handler[] loadPipeline;

    public void runPipeline(RepoForm repoForm){
       new AnaliserHandler().process(new AnalyserHandlerParam(new StoreHandler().process(new StoreHandlerParam(new CloneHandler().process(new CloneHandlerParam(repoForm))))));
        //new AnaliserHandler().process(new AnalyserHandlerParam(RepositoryVersion.find.byId(1L)));
    }



}
