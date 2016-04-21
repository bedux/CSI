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
public class AnalysePackageColor implements Analyser<Integer> {




    public AnalysePackageColor() {

    }


    /***
     * Compute the color of a given Component. It cast the type and call different function depending the type of the Component
     *
     * @param component
     * @return only a random number, NOT USE!
     */
    @Override
    public Integer analysis(Component component) {

        component.getComponentList().stream().forEach((x) -> x.applyFunction((new AnalysePackageColor())::analysis));

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
            packages.getFeatures().setColorMetrics(packages.getComponentList().stream().map(x->x.getFeatures().getColorMetrics()).reduce(0.0f,(x,y)->x+y) / (float)packages.getComponentList().size());
    }

    /**
     * Compute the color of a data file. If is not a java file, if assign a default color
     * @param dataFile The Component
     */
    private void computeColor(DataFile dataFile) {



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
