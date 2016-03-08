package logics.analyzer.analysis;

import interfaces.Analyser;
import interfaces.Component;
import logics.models.tools.MaximumMinimumData;

/**
 * Created by bedux on 03/03/16.
 */
public class MaximumDimentionAnalyser implements Analyser<MaximumMinimumData> {
    @Override
    public MaximumMinimumData analysis(Component c) {
        if(c.getComponentList().size()==0){
            return new MaximumMinimumData(c.getFeatures().getWidth(),c.getFeatures().getHeight(),c.getFeatures().getDeep());
        }

        MaximumMinimumData[] streamResult = c.getComponentList().stream().map((x) -> {
            return x.applyFunction((new MaximumDimentionAnalyser())::analysis);
        }).toArray(x->new MaximumMinimumData[x]);

        MaximumMinimumData myMax = streamResult[0];
        for(  MaximumMinimumData mh:streamResult){


            myMax.setOnlyIfMaxMinDepth(mh.maxDepth);
            myMax.setOnlyIfMaxMinHeight(mh.maxHeight);
            myMax.setOnlyIfMaxMinWidth(mh.maxWidth);

            myMax.setOnlyIfMaxMinDepth(mh.minDepth);
            myMax.setOnlyIfMaxMinHeight(mh.minHeight);
            myMax.setOnlyIfMaxMinWidth(mh.minWidth);
        }
        return myMax;
    }
}

