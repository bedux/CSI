package logics.analyzer.analysis;

import interfaces.Analyser;
import interfaces.Component;
import logics.analyzer.DataFile;
import logics.models.query.IComputeAttributeContainer;
import play.Logger;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Analyser that retrieve information for each file
 */
public class LoadFromDatabase implements Analyser<CompletableFuture<Integer>> {

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
    public CompletableFuture<Integer> analysis(Component component) {
        List<CompletableFuture<Integer>> res =
                component.getComponentList().stream().map((x) ->
                                x.applyFunction((new LoadFromDatabase(widthQuery, heightQuery, colorQuery))::analysis)
                ).collect(Collectors.toList());

        CompletableFuture<Void> allDoneFuture = CompletableFuture.allOf(res.toArray(new CompletableFuture[res.size()]));
        if (component instanceof DataFile) {
                return allDoneFuture.thenApply((x) ->
                        computeMetricsOfComponent((DataFile) component));
        }
        return allDoneFuture.thenApply(x->1);

    }


    /***
     *
     * @param dataFile set the component feature by a given query,Note: set only the matrix value no the actual size
     */
    private int computeMetricsOfComponent(DataFile dataFile) {
        Logger.info("Load From Data => "+ dataFile.getFeatures().getPath());

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
        return 1;
    }
}

