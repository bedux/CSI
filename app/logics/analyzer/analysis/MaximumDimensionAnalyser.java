package logics.analyzer.analysis;

import interfaces.Analyser;
import interfaces.Component;
import logics.models.tools.MaximumMinimumData;

public class MaximumDimensionAnalyser implements Analyser<MaximumMinimumData> {

    /***
     *
     * @param component
     * @return the maximumMinimum of the subtree
     */
    @Override
    public MaximumMinimumData analysis(Component component) {

        if (component.getComponentList().size() == 0) {
            return new MaximumMinimumData(component.getFeatures().getWidthMetrics(), component.getFeatures().getHeightMetrics(), component.getFeatures().getDepthMetrics(), component.getFeatures().getColorMetrics());
        }
        MaximumMinimumData[] streamResult = component.getComponentList().parallelStream().map((x) ->
                        x.applyFunction((new MaximumDimensionAnalyser())::analysis)
        ).toArray(x -> new MaximumMinimumData[x]);

        MaximumMinimumData myMax = streamResult[0];
        for (int i = 1; i < streamResult.length; i++) {
            MaximumMinimumData mh = streamResult[i];
            myMax.merge(mh.getDepths(), mh.getHeights(), mh.getWidths(), mh.getColors());

            myMax.setOnlyIfMaxMinDepth(mh.maxDepth);
            myMax.setOnlyIfMaxMinHeight(mh.maxHeight);
            myMax.setOnlyIfMaxMinWidth(mh.maxWidth);
            myMax.setOnlyIfMaxMinColor(100);

            myMax.setOnlyIfMaxMinDepth(mh.minDepth);
            myMax.setOnlyIfMaxMinHeight(mh.minHeight);
            myMax.setOnlyIfMaxMinWidth(mh.minWidth);
            myMax.setOnlyIfMaxMinColor(0);

        }
        return myMax;
    }
}

