package logics.analyzer.analysis;

import interfaces.Analyser;
import interfaces.Component;
import logics.analyzer.BinaryFile;
import logics.analyzer.DataFile;
import logics.analyzer.Package;

import java.util.List;
import java.util.stream.Collectors;

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
            List<Float > list =
                    packages.getComponentList().stream()
                            .map(x->x.getFeatures().getColorMetrics())
                            .filter(x -> x >= 0)
                            .collect(Collectors.toList());

        if(list.size()==0){
            packages.getFeatures().setColorMetrics(-1);
            packages.getFeatures().setBuildingType(ColoringAnalyser.BuildingType.NO_CARE.ordinal());


        }else {
            packages.getFeatures().setColorMetrics(
                    list
                    .stream()
                    .reduce(0.0f, (x, y) -> x + y)
                            / (float) packages.getComponentList().size());

            packages.getFeatures().setBuildingType(ColoringAnalyser.BuildingType.COLOR.ordinal());

        }
    }

    /**
     * Compute the color of a data file. If is not a java file, if assign a default color
     * @param dataFile The Component
     */
    private void computeColor(DataFile dataFile) {
        if (dataFile.getFeatures().getPath().indexOf(".java") != dataFile.getFeatures().getPath().length() - 5) {
            dataFile.getFeatures().setColorMetrics(-1);

        }
        dataFile.getFeatures().setBuildingType(ColoringAnalyser.BuildingType.INVISIBLE.ordinal());
        dataFile.getFeatures().setColor(-1);

    }

    /**
     * Compute the color of a data file. If is not a java file, if assign a default color
     * @param binaryFile The Component
     */
    private void computeColor(BinaryFile binaryFile) {

        binaryFile.getFeatures().setBuildingType(ColoringAnalyser.BuildingType.INVISIBLE.ordinal());
        binaryFile.getFeatures().setColor(-1);
        binaryFile.getFeatures().setColorMetrics(-1);



    }


}
