package logics.analyzer.analysis;

import interfaces.Analyser;
import interfaces.Component;
import logics.analyzer.BinaryFile;
import logics.analyzer.DataFile;
import logics.analyzer.Package;
import logics.models.tools.MaximumMinimumData;

/**
 * Created by bedux on 03/03/16.
 */
public class ColoringAnalyserOnlyPackage implements Analyser<Integer> {

    private float max = 0;
    private MaximumMinimumData maximumMinimumData;

    /***
     *
     * @param max tha max remoteness used to compute the package coloring
     * @param maximumMinimumData the bounding value of teh metrics of the project
     */
    public ColoringAnalyserOnlyPackage(float max, MaximumMinimumData maximumMinimumData) {
        this.max = max;
        this.maximumMinimumData = maximumMinimumData;
    }


    /***
     * Compute the color of a given Component. It cast the type and call different function depending the type of the Component
     *
     * @param component
     * @return only a random number, NOT USE!
     */
    @Override
    public Integer analysis(Component component) {

        component.getComponentList().stream().forEach((x) -> x.applyFunction((new ColoringAnalyserOnlyPackage(max, maximumMinimumData))::analysis));

        if (component instanceof BinaryFile) {
            computeColor((BinaryFile)component);
        } else if (component instanceof DataFile) {
            computeColor((DataFile) component);
        } else if (component instanceof Package) {
            computeColor((Package) component);
        }
        return 0;
    }

    /***
     * Compute the color of the package, using remoteness
     * @param packages The Component
     */
    private void computeColor(Package packages) {

//        if (packages.getFeatures().getRemoteness() == 0) {
//            packages.getFeatures().setColor(0);
//            packages.getFeatures().setBuildingType(4);
//        } else {
//            float color = packages.getFeatures().getRemoteness() / ((float) max);
//            packages.getFeatures().setColor(color + 0.01f);
            packages.getFeatures().setBuildingType(2);

        packages.getFeatures().setColor(packages.getFeatures().getColorMetrics()/ max);

//        }
    }

    /**
     * Compute the color of a data file. If is not a java file, if assign a default color
     * @param dataFile The Component
     */
    private void computeColor(DataFile dataFile) {

        if (dataFile.getFeatures().getPath().indexOf(".java") != dataFile.getFeatures().getPath().length() - 5) {
            dataFile.getFeatures().setColor(0);
            dataFile.getFeatures().setBuildingType(1);

        } else {

            // dataFile.getFeatures().setColor(dataFile.getFeatures().getColorMetrics()/ maximumMinimumData.maxColor);
            dataFile.getFeatures().setBuildingType(8);
        }


    }

    /**
     * Compute the color of a data file. If is not a java file, if assign a default color
     * @param binaryFile The Component
     */
    private void computeColor(BinaryFile binaryFile) {

        binaryFile.getFeatures().setBuildingType(0);
        binaryFile.getFeatures().setColor(-1);


    }


}
