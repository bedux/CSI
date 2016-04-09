package logics.analyzer.analysis;

import interfaces.Analyser;
import interfaces.Component;
import logics.renderTools.BinaryTreePack;
import logics.renderTools.BoundingBox;

import java.util.Arrays;

/**
 * Created by bedux on 03/03/16.
 */
public class PackingAnalyzer implements Analyser<Integer> {

    /**
     *
     * @param component where starting the packing
     * @return random Number, NOT USE!
     */
    @Override
    public Integer analysis(Component component) {
        if (component.getComponentList().size() == 0) return 0;

        component.getComponentList().stream().forEach((x) -> x.applyFunction((new PackingAnalyzer())::analysis));
        BinaryTreePack r;
        Component[] strm = component.getComponentList().stream().sorted((x, y) -> {
            float a = x.getFeatures().getWidth() * x.getFeatures().getDepth();
            float b = y.getFeatures().getWidth() * y.getFeatures().getDepth();

            if (a == b) return 0;
            else return a > b ? -1 : 1;
        }).toArray(x -> new Component[x]);
        r = new BinaryTreePack(strm[0]);
        Arrays.stream(strm).forEach((x -> {
            if (x.getFeatures().getWidth() > 0 && x.getFeatures().getDepth() > 0)
                r.insert(x);
        }));


        float w = 0;
        float d = 0;
        for (Component x : component.getComponentList()) {
            w = w < x.getFeatures().getBoundingBox().getRight() ? x.getFeatures().getBoundingBox().getRight() : w;
            d = d < x.getFeatures().getBoundingBox().getButton() ? x.getFeatures().getBoundingBox().getButton() : d;
        }
        component.getFeatures().getBoundingBox().setBB(new BoundingBox(d + component.getFeatures().gap, w + component.getFeatures().gap));
        return 1;

    }

}
