package logics.analyzer.analysis;

import interfaces.Analyser;
import interfaces.Component;
import logics.analyzer.DataFile;
import logics.models.query.IComputeAttributeContainer;
import play.Logger;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Analyser that retrieve information for each file
 */
public class LoadFromDatabase implements Analyser<CompletableFuture<Integer>> {

    private final  IComputeAttributeContainer widthQuery;
    private final IComputeAttributeContainer heightQuery;
    private final IComputeAttributeContainer colorQuery;

    /***
     *
     * @param width the with query  used to get the metrics
     * @param height the with query  used to get the metrics
     * @param color the with query  used to get the metrics
     */
    public LoadFromDatabase(IComputeAttributeContainer width, IComputeAttributeContainer height, IComputeAttributeContainer color) {
        this.widthQuery = width.clone();
        this.heightQuery = height.clone();
        this.colorQuery = color.clone();
    }


    /***
     *
     * @param component The current component
     * @return random number, NOT USE!
     */
    @Override
    public CompletableFuture<Integer> analysis(Component component) {
        CompletableFuture[] res =
                component.getComponentList().stream().map(
                        (x) -> CompletableFuture.supplyAsync(() -> x.applyFunction((new LoadFromDatabase(widthQuery.clone(), heightQuery.clone(), colorQuery.clone()))::analysis)
                        )
                ).toArray(CompletableFuture[]::new);

        CompletableFuture<Void> allDoneFuture = CompletableFuture.allOf(res);
        allDoneFuture.join();
        return allDoneFuture.thenApply(
                v -> {
                    if (component instanceof DataFile) {
                        return computeMetricsOfComponent((DataFile) component);
                    } else {
                        return 1;
                    }
                }
        );
    }


    /***
     *
     * @param dataFile set the component feature by a given query,Note: set only the matrix value no the actual size
     */
    private int computeMetricsOfComponent(DataFile dataFile) {
        Logger.info("Load From Data => "+ dataFile.getFeatures().getPath()+" Thread id=>"+Thread.currentThread().getName());

        String fn = dataFile.getFeatures().getPath().substring(dataFile.getFeatures().getPath().lastIndexOf(".") + 1);


        if (fn.indexOf("java") == 0) {
            String currentPath = dataFile.getFeatures().getPath();
            int width = (int) widthQuery.clone().executeAndGetResult(currentPath);
            dataFile.getFeatures().setWidthMetrics(width);
            dataFile.getFeatures().setDepthMetrics(width);
            dataFile.getFeatures().setHeightMetrics(heightQuery.executeAndGetResult(currentPath));
            long color = colorQuery.executeAndGetResult(currentPath);
            dataFile.getFeatures().setColorMetrics(color);
        }
        return 1;
    }
}

