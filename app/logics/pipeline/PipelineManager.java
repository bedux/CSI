package logics.pipeline;

import logics.models.form.RepoForm;
import logics.pipeline.analayser.AnalyserHandler;
import logics.pipeline.analayser.AnalyserHandlerParam;
import logics.pipeline.clone.CloneHandler;
import logics.pipeline.clone.CloneHandlerParam;
import logics.pipeline.storing.StoreHandler;
import logics.pipeline.storing.StoreHandlerParam;


public class PipelineManager {
    public void runPipeline(RepoForm repoForm) {
        new AnalyserHandler().process(new AnalyserHandlerParam(new StoreHandler().process(new StoreHandlerParam(new CloneHandler().process(new CloneHandlerParam(repoForm))))));
    }
}
