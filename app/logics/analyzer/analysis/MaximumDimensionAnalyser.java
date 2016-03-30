package logics.analyzer.analysis;

import interfaces.Analyser;
import interfaces.Component;
import logics.models.tools.MaximumMinimumData;

public class MaximumDimensionAnalyser implements Analyser<MaximumMinimumData> {

    @Override
    public MaximumMinimumData analysis(Component c) {

        if (c.getComponentList().size() == 0) {


            return new MaximumMinimumData(c.getFeatures().getWidthMetrics(), c.getFeatures().getHeightMetrics(), c.getFeatures().getDepthMetrics(), c.getFeatures().getColorMetrics());
        }
        MaximumMinimumData[] streamResult = c.getComponentList().stream().map((x) ->
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

