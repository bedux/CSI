package logics.analyzer.analysis;

import interfaces.Analyser;
import interfaces.Component;

/**
 * Created by bedux on 03/03/16.
 */
public class PackageColorAnalyser implements Analyser<Float> {

    /**
     * Computing the remoteness of each component of the tree and return the maximum of the current subtree
     * @param component the current component
     * @return Maximum remoteness of the current subtree
     */
    @Override
    public Float analysis(Component component) {

        if (component.getComponentList().size() == 0) return 0f;
        float maxOfChildren =  component.getComponentList().stream().map((x) -> {
            return x.applyFunction(new PackageColorAnalyser()::analysis);
        }).max(Float::compare).get();
        return Math.max(maxOfChildren,component.getFeatures().getColorMetrics());
    }
}
