package logics.analyzer.analysis;

import interfaces.Analyser;
import interfaces.Component;

/**
 * Created by bedux on 03/03/16.
 */
public class DepthAnalyser implements Analyser<Integer> {

    @Override
    public Integer analysis(Component c) {
        if(c.getComponentList().size()==0) return c.getFeatures().getRemotness();
        return c.getComponentList().stream().map((x) ->{
            x.getFeatures().setRemoteness(c.getFeatures().getRemotness()+1);
            return x.applyFunction((new DepthAnalyser())::analysis);
        }).max(Integer::compare).get();
    }
}
