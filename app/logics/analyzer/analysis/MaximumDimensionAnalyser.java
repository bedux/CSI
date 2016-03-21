package logics.analyzer.analysis;

import interfaces.Analyser;
import interfaces.Component;
import logics.models.tools.MaximumMinimumData;

public class MaximumDimensionAnalyser implements Analyser<MaximumMinimumData> {

    @Override
    public MaximumMinimumData analysis(Component c) {

        if(c.getComponentList().size()==0){
            return new MaximumMinimumData(c.getFeatures().getWidth(),c.getFeatures().getHeight(),c.getFeatures().getDeep(),c.getFeatures().getColor());
        }
        MaximumMinimumData[] streamResult = c.getComponentList().stream().map((x) ->
                        x.applyFunction((new MaximumDimensionAnalyser())::analysis)
        ).toArray(x -> new MaximumMinimumData[x]);

        MaximumMinimumData myMax = streamResult[0];
        for(  MaximumMinimumData mh:streamResult){

            myMax.setOnlyIfMaxMinDepth(mh.maxDepth);
            myMax.setOnlyIfMaxMinHeight(mh.maxHeight);
            myMax.setOnlyIfMaxMinWidth(mh.maxWidth);
            myMax.setOnlyIfMaxMinColor(mh.maxColor);

            myMax.setOnlyIfMaxMinDepth(mh.minDepth);
            myMax.setOnlyIfMaxMinHeight(mh.minHeight);
            myMax.setOnlyIfMaxMinWidth(mh.minWidth);
            myMax.setOnlyIfMaxMinColor(mh.minColor);

        }
        return myMax;
    }
}

