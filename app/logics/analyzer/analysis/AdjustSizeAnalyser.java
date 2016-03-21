package logics.analyzer.analysis;

import interfaces.Analyser;
import interfaces.Component;
import logics.analyzer.*;
import logics.analyzer.Package;
import logics.models.tools.MaximumMinimumData;


public class AdjustSizeAnalyser implements Analyser<Integer> {

    private static float division = 4f;
    private static  final float heightPackage = 20;
    private static  final float heightBinaryFile = 5;
    private static  final float maxWidth = 50;
    private static  final float maxDeep = 50;
    private static  final float maxHeight = 50;




    MaximumMinimumData maximumMinimumData;
    public AdjustSizeAnalyser(MaximumMinimumData maximumMinimumData){
        this.maximumMinimumData=maximumMinimumData;
    }

    @Override
    public Integer analysis(Component c) {
        c.getComponentList().stream().forEach((x) -> x.applyFunction((new AdjustSizeAnalyser(maximumMinimumData))::analysis));
        if (c instanceof BinaryFile) {
            c.getFeatures().gap = 0.4f;
            c.getFeatures().setHeight(heightBinaryFile);
            c.getFeatures().setWidth(5);
            c.getFeatures().setDeep(5);


        } else if (c instanceof DataFile) {
           analysisCast((DataFile)c);
        }else if (c instanceof logics.analyzer.Package){
            analysisCast((Package) c);
        }

        return 0;

    }





    private Integer analysisCast(Package p){
        p.getFeatures().setHeight(heightPackage);
        return 1;
    }

    private Integer analysisCast(DataFile c){

        String fn = c.getFeatures().getPath().substring(c.getFeatures().getPath().lastIndexOf(".") + 1);
        if(fn.indexOf("java")==-1){
            c.getFeatures().gap = 1f;
            c.getFeatures().setHeight(heightBinaryFile);
            c.getFeatures().setWidth(10);
            c.getFeatures().setDeep(10);

            return 0;
        }

        float myDepth = (c.getFeatures().getDeep()-maximumMinimumData.minDepth);
        float deltaDepth = (maximumMinimumData.maxDepth-maximumMinimumData.minDepth)/ division;
        float depthStepNumber = (int)( myDepth/deltaDepth)+1;
        c.getFeatures().setDeep(depthStepNumber * maxDeep);



        float myWidth = (c.getFeatures().getWidth()-maximumMinimumData.minWidth);
        float deltaWidth = (maximumMinimumData.maxWidth-maximumMinimumData.minWidth)/ division;
        float widthStepNumber =(int) ( myWidth/deltaWidth)+1;
        c.getFeatures().setWidth(widthStepNumber * maxWidth);

        float myHeight = (c.getFeatures().getHeight()-maximumMinimumData.minHeight);
        float deltaHeight = (maximumMinimumData.maxHeight-maximumMinimumData.minHeight)/ division;
        float depthStepHeight = ( myHeight/deltaHeight)+1;

        c.getFeatures().setHeight(depthStepHeight * maxHeight);



        return 1;

    }

}
