package logics.analyzer.analysis;

import interfaces.Analyser;
import interfaces.Component;
import logics.analyzer.BinaryFile;
import logics.analyzer.DataFile;
import logics.models.query.IComputeAttributeContainer;

/**
 * Analyser that retrieve information for each file
 */
public class LoadFromDatabase implements Analyser<Integer> {

    IComputeAttributeContainer widthQuery;
    IComputeAttributeContainer heightQuery;
    IComputeAttributeContainer colorQuery;

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


    @Override
    public Integer analysis(Component value) {
        value.getComponentList().stream().forEach((x) -> x.applyFunction((new LoadFromDatabase(widthQuery, heightQuery, colorQuery))::analysis));
        if (value instanceof BinaryFile) {

        } else if (value instanceof DataFile) {
            analysisCast((DataFile) value);
        }
        return 1;
    }

    private void analysisCast(DataFile c) {
        String fn = c.getFeatures().getPath().substring(c.getFeatures().getPath().lastIndexOf(".") + 1);


        if (fn.indexOf("java") == 0) {
            String currentPath = c.getFeatures().getPath();
            int width = (int) widthQuery.executeAndGetResult(currentPath);
            c.getFeatures().setWidthMetrics(width);
            c.getFeatures().setDepthMetrics(width);
            c.getFeatures().setHeightMetrics(heightQuery.executeAndGetResult(currentPath));
            long color = colorQuery.executeAndGetResult(currentPath);
            c.getFeatures().setColorMetrics(color);
        }
    }
}

