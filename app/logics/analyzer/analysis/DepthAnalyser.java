package logics.analyzer.analysis;

import interfaces.Analyser;
import interfaces.Component;

/**
 * Created by bedux on 03/03/16.
 */
public class DepthAnalyser implements Analyser<Integer> {

    /**
     * Computing the remoteness of each component of the tree and return the maximum of the current subtree
     * @param component the current component
     * @return Maximum remoteness of the current subtree
     */
    @Override
    public Integer analysis(Component component) {
        if (component.getComponentList().size() == 0) return component.getFeatures().getRemoteness();
        return component.getComponentList().stream().map((x) -> {
            x.getFeatures().setRemoteness(component.getFeatures().getRemoteness() + 1);
            return x.applyFunction((new DepthAnalyser())::analysis);
        }).max(Integer::compare).get();
    }
}
