package logics.analyzer.analysis;

import interfaces.Analyser;
import interfaces.Component;
import logics.analyzer.BinaryFile;
import logics.analyzer.DataFile;
import logics.analyzer.Package;
import logics.models.tools.MaximumMinimumData;


public class AdjustSizeAnalyser implements Analyser<Integer> {


    private static final float unit = 20;


    private MaximumMinimumData maximumMinimumData;

    public AdjustSizeAnalyser(MaximumMinimumData maximumMinimumData) {
        this.maximumMinimumData = maximumMinimumData;
    }

    @Override
    public Integer analysis(Component c) {
        c.getComponentList().stream().forEach((x) -> x.applyFunction((new AdjustSizeAnalyser(maximumMinimumData))::analysis));

        if (c instanceof BinaryFile) {
            c.getFeatures().setHeight(unit);
            c.getFeatures().setWidth(unit);
            c.getFeatures().setDeep(unit);


        } else if (c instanceof DataFile) {
            analysisCast((DataFile) c);
        } else if (c instanceof logics.analyzer.Package) {
            analysisCast((Package) c);
        }

        return 0;

    }


    private Integer analysisCast(Package p) {
        p.getFeatures().setHeight(unit);

        return 1;
    }

    private Integer analysisCast(DataFile c) {

        String fn = c.getFeatures().getPath().substring(c.getFeatures().getPath().lastIndexOf(".") + 1);
        if (fn.indexOf("java") == -1) {
            c.getFeatures().setHeight(unit);
            c.getFeatures().setWidth(unit);
            c.getFeatures().setDeep(unit);

            return 0;
        }


        c.getFeatures().setDeep(c.getFeatures().getRendererDeep() * unit + unit);
        c.getFeatures().setWidth(c.getFeatures().getRendererWidth() * unit + unit);
        c.getFeatures().setHeight(c.getFeatures().getHeight() * unit + unit);

//        float myDepth = (c.getFeatures().getDeep()-maximumMinimumData.minDepth);
//        float deltaDepth = (maximumMinimumData.maxDepth-maximumMinimumData.minDepth)/ division;
//        int depthStepNumber = (int)( myDepth/deltaDepth)+1;
//        switch(depthStepNumber){
//            case 1:
//                c.getFeatures().gap = 60;
//                c.getFeatures().setDeep(50);
//                break;
//            case 2:
//                c.getFeatures().gap = 110;
//                c.getFeatures().setDeep(75);
//                break;
//            case 3:
//                c.getFeatures().gap = 200;
//                c.getFeatures().setDeep(100);
//                break;
//        }

//        float [] res = maximumMinimumData.getDepthDivision(5);
//        if(c.getFeatures().getDeep() <= res[0]){
//            c.getFeatures().gap = 20;
//            c.getFeatures().setDeep(20);
//        }else  if(c.getFeatures().getDeep() <= res[1]){
//            c.getFeatures().gap = 40;
//            c.getFeatures().setDeep(40);
//        }else  if(c.getFeatures().getDeep() <= res[2]){
//            c.getFeatures().gap = 50;
//            c.getFeatures().setDeep(100);
//        }else  if(c.getFeatures().getDeep() <= res[3]){
//            c.getFeatures().gap = 100;
//            c.getFeatures().setDeep(200);
//        }else  if(c.getFeatures().getDeep() <= res[4]){
//            c.getFeatures().gap = 120;
//            c.getFeatures().setDeep(400);
//        }else {
//            c.getFeatures().gap = 160;
//            c.getFeatures().setDeep(800);
//        }

//        float myWidth = (c.getFeatures().getWidth()-maximumMinimumData.minWidth);
//        float deltaWidth = (maximumMinimumData.maxWidth-maximumMinimumData.minWidth)/ division;
//        int widthStepNumber =(int) ( myWidth/deltaWidth)+1;
//        c.getFeatures().setWidth(widthStepNumber * maxWidth);
//        switch(widthStepNumber){
//            case 1:
//                c.getFeatures().setWidth(50);
//                break;
//            case 2:
//                c.getFeatures().setWidth(75);
//                break;
//            case 3:
//                c.getFeatures().setWidth(100);
//                break;
//
//
//
//        }
//         res = maximumMinimumData.getWidthDivision(5);
//        if(c.getFeatures().getWidth() <= res[0]){
//            c.getFeatures().setWidth(20);
//        }else  if(c.getFeatures().getWidth() <= res[1]){
//            c.getFeatures().setWidth(40);
//        }else  if(c.getFeatures().getWidth() <= res[2]){
//            c.getFeatures().setWidth(100);
//        }else  if(c.getFeatures().getWidth() <= res[3]){
//            c.getFeatures().setWidth(200);
//        }else  if(c.getFeatures().getWidth() <= res[4]){
//            c.getFeatures().setWidth(400);
//        }else{
//            c.getFeatures().setWidth(800);
//        }
//

//        float myHeight = (c.getFeatures().getHeight()-maximumMinimumData.minHeight);
//        float deltaHeight = (maximumMinimumData.maxHeight-maximumMinimumData.minHeight)/ division;
//        int depthStepHeight = (int) ( myHeight/deltaHeight)+1;
//
//
//        switch(depthStepHeight){
//            case 1:
//                c.getFeatures().setHeight(50);
//                break;
//            case 2:
//                c.getFeatures().setHeight(150);
//                break;
//
//            case 3:
//                c.getFeatures().setHeight(350);
//                break;
//
//
//        }
//
//       res = maximumMinimumData.getHeightDivision(5);
//        if(c.getFeatures().getHeight() <= res[0]){
//            c.getFeatures().setHeight(20);
//        }else  if(c.getFeatures().getHeight() <= res[1]){
//            c.getFeatures().setHeight(40);
//        }else  if(c.getFeatures().getHeight() <= res[2]){
//            c.getFeatures().setHeight(100);
//        }else  if(c.getFeatures().getHeight() <= res[3]){
//            c.getFeatures().setHeight(200);
//        }else  if(c.getFeatures().getHeight() <= res[4]){
//            c.getFeatures().setHeight(400);
//        }else{
//            c.getFeatures().setHeight(800);
//        }
//
        return 1;

    }

}
