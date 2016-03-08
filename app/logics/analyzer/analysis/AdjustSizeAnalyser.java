package logics.analyzer.analysis;

import interfaces.Analyser;
import interfaces.Component;
import logics.analyzer.*;
import logics.analyzer.Package;
import logics.models.tools.MaximumMinimumData;

/**
 * Created by bedux on 03/03/16.
 */
public class AdjustSizeAnalyser implements Analyser<Integer> {

    private static float divison = 5f;
    MaximumMinimumData maximumMinimumData;
    public AdjustSizeAnalyser(MaximumMinimumData maximumMinimumData){
        this.maximumMinimumData=maximumMinimumData;
    }
    @Override
    public Integer analysis(Component c) {
        c.getComponentList().stream().forEach((x) -> x.applyFunction((new AdjustSizeAnalyser(maximumMinimumData))::analysis));
        int n = 0;
        if (c instanceof BinaryFile) {
            c.getFeatures().setHeight(10);
        } else if (c instanceof DataFile) {
            n=analysisCast((DataFile)c);
        }else if (c instanceof logics.analyzer.Package){
            n=analysisCast((Package) c);
        }

        return n;

    }


    private Integer analysisCast(Package p){
        p.getFeatures().setHeight(10);
        return 1;
    }

    private Integer analysisCast(DataFile c){

        float myDepth = (c.getFeatures().getDeep()-maximumMinimumData.minDepth);
        float deltaDepth = (maximumMinimumData.maxDepth-maximumMinimumData.minDepth)/divison;
        float depthStepNumber = (int)( myDepth/deltaDepth)+1;
        c.getFeatures().setDeep(depthStepNumber * 30);



        float myWidth = (c.getFeatures().getWidth()-maximumMinimumData.minWidth);
        float deltaWidth = (maximumMinimumData.maxWidth-maximumMinimumData.minWidth)/divison;
        float depthStepWidth =(int) ( myWidth/deltaWidth)+1;
        c.getFeatures().setWidth(depthStepWidth * 30);

        float myHeight = (c.getFeatures().getHeight()-maximumMinimumData.minHeight);
        float deltaHeight = (maximumMinimumData.maxHeight-maximumMinimumData.minHeight)/divison;
        float depthStepHeight = ( myHeight/deltaHeight)+1;

        c.getFeatures().setHeight(depthStepHeight * 40);



        return 1;

    }

}
