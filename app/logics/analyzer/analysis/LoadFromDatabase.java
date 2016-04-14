package logics.analyzer.analysis;

import interfaces.Analyser;
import interfaces.Component;
import logics.analyzer.DataFile;
import logics.models.query.IComputeAttributeContainer;

/**
 * Analyser that retrieve information for each file
 */
public class LoadFromDatabase implements Analyser<Integer> {

    private  IComputeAttributeContainer widthQuery;
    private IComputeAttributeContainer heightQuery;
    private IComputeAttributeContainer colorQuery;

    /***
     *
     * @param width the with query  used to get the metrics
     * @param height the with query  used to get the metrics
     * @param color the with query  used to get the metrics
     */
    public LoadFromDatabase(IComputeAttributeContainer width, IComputeAttributeContainer height, IComputeAttributeContainer color) {
        this.widthQuery = width;
        this.heightQuery = height;
        this.colorQuery = color;
    }


    /***
     *
     * @param component The current component
     * @return random number, NOT USE!
     */
    @Override
    public Integer analysis(Component component) {
        component.getComponentList().stream().forEach((x) -> x.applyFunction((new LoadFromDatabase(widthQuery, heightQuery, colorQuery))::analysis));

        if (component instanceof DataFile) {
            computeMetricsOfComponent((DataFile) component);
        }
        return 1;
    }


    /***
     *
     * @param dataFile set the component feature by a given query,Note: set only the matrix value no the actual size
     */
    private void computeMetricsOfComponent(DataFile dataFile) {
        String fn = dataFile.getFeatures().getPath().substring(dataFile.getFeatures().getPath().lastIndexOf(".") + 1);


        if (fn.indexOf("java") == 0) {
            String currentPath = dataFile.getFeatures().getPath();
            int width = (int) widthQuery.executeAndGetResult(currentPath);
            dataFile.getFeatures().setWidthMetrics(width);
            dataFile.getFeatures().setDepthMetrics(width);
            dataFile.getFeatures().setHeightMetrics(heightQuery.executeAndGetResult(currentPath));
            long color = colorQuery.executeAndGetResult(currentPath);
            dataFile.getFeatures().setColorMetrics(color);
        }
    }
}

