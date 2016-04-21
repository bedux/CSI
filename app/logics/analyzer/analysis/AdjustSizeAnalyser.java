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

    /***
     * Analyse the current component and adjust the size whit the unit metrics
     * @param component
     * @return NOT USED
     */
    @Override
    public Integer analysis(Component component) {
        component.getComponentList().parallelStream().forEach((x) -> x.applyFunction((new AdjustSizeAnalyser(maximumMinimumData))::analysis));
        if (component instanceof BinaryFile) {
            component.getFeatures().setHeight(unit);
            component.getFeatures().setWidth(unit);
            component.getFeatures().setDepth(unit);
        } else if (component instanceof DataFile) {
            analysisCast((DataFile) component);
        } else if (component instanceof logics.analyzer.Package) {
            analysisCast((Package) component);
        }

        return 0;

    }


    /***
     * Set the height of a package
     * @param p
     * @return
     */
    private Integer analysisCast(Package p) {
        p.getFeatures().setHeight(unit);
        return 1;
    }

    /**
     *
     * Adjust the size of a data file
     * @param dataFile
     * @return
     */
    private Integer analysisCast(DataFile dataFile) {

        String fn = dataFile.getFeatures().getPath().substring(dataFile.getFeatures().getPath().lastIndexOf(".") + 1);
        if (fn.indexOf("java") == -1) {
            dataFile.getFeatures().setHeight(unit);
            dataFile.getFeatures().setWidth(unit);
            dataFile.getFeatures().setDepth(unit);

            return 0;
        }

        dataFile.getFeatures().setDepth(dataFile.getFeatures().getDepthMetrics() * unit + unit);
        dataFile.getFeatures().setWidth(dataFile.getFeatures().getWidthMetrics() * unit + unit);
        dataFile.getFeatures().setHeight(dataFile.getFeatures().getHeightMetrics() * unit + unit);

        return 1;

    }

}
