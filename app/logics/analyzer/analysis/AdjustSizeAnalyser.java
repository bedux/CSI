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
        c.getComponentList().parallelStream().forEach((x) -> x.applyFunction((new AdjustSizeAnalyser(maximumMinimumData))::analysis));

        if (c instanceof BinaryFile) {
            c.getFeatures().setHeight(unit);
            c.getFeatures().setWidth(unit);
            c.getFeatures().setDepth(unit);


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
            c.getFeatures().setDepth(unit);

            return 0;
        }

        c.getFeatures().setDepth(c.getFeatures().getDepthMetrics() * unit + unit);
        c.getFeatures().setWidth(c.getFeatures().getWidthMetrics() * unit + unit);
        c.getFeatures().setHeight(c.getFeatures().getHeightMetrics() * unit + unit);

        return 1;

    }

}
