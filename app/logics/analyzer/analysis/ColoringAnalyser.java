package logics.analyzer.analysis;

import interfaces.Analyser;
import interfaces.Component;
import logics.analyzer.*;
import logics.analyzer.Package;
import logics.models.tools.MaximumMinimumData;
import logics.renderTools.Point3d;

/**
 * Created by bedux on 03/03/16.
 */
public class ColoringAnalyser implements Analyser<Integer>{

    private int max = 0;
    private MaximumMinimumData maximumMinimumData;

    public ColoringAnalyser(int max, MaximumMinimumData maximumMinimumData){
        this.max = max;
        this.maximumMinimumData=maximumMinimumData;
    }
    @Override
    public Integer analysis(Component c) {

        c.getComponentList().stream().forEach((x) -> x.applyFunction((new ColoringAnalyser(max,maximumMinimumData))::analysis));
        if (c instanceof BinaryFile) {
            c.getFeatures().setBuildingType(0);
            c.getFeatures().setColor(-1);
        } else if (c instanceof DataFile) {
            analysisCast((DataFile) c);
        }else if (c instanceof logics.analyzer.Package){
            analysisCast((Package) c);
        }

        return 0;
    }

    private void analysisCast(Package p){

        float color = p.getFeatures().getRemoteness()/((float)max);
        p.getFeatures().setColor(color+0.01f);
    }

    private void analysisCast(DataFile p){
        //some computation about the color!

        if(p.getFeatures().getPath().indexOf(".java")!=p.getFeatures().getPath().length()-5) {

            p.getFeatures().setColor(-1);
            p.getFeatures().setBuildingType(1);

        }else {
            float f1 = p.getFeatures().getColor() - this.maximumMinimumData.minColor;
            float f2 = this.maximumMinimumData.maxColor - this.maximumMinimumData.minColor;
            float f3 = f2 / 2;
            p.getFeatures().setColor(-1);
            p.getFeatures().setBuildingType((int)(f1/f3)+2);
        }
        //end computation
        //p.getFeatures().setColor(0);

    }

}
