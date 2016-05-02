package logics.analyzer.analysis;

import interfaces.Analyser;
import interfaces.Component;
import logics.analyzer.DataFile;
import play.Logger;

import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * Analyser that retrieve information for each file
 */
public class LoadFromDatabase implements Analyser<Integer> {

    private final Function<String,Long> widthQuery;
    private final Function<String,Long> heightQuery;
    private final Function<String,Long> colorQuery;

    /***
     *
     * @param width the with query  used to get the metrics
     * @param height the with query  used to get the metrics
     * @param color the with query  used to get the metrics
     */
    public LoadFromDatabase(Function<String,Long> width, Function<String,Long> height, Function<String,Long> color) {
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
//        CompletableFuture[] res =
//                component.getComponentList().stream().map(
//                        (x) -> CompletableFuture.supplyAsync(() -> x.applyFunction((new LoadFromDatabase(widthQuery.clone(), heightQuery.clone(), colorQuery.clone()))::analysis),ThreadManager.instance().getExecutor()
//                        )
//                ).toArray(CompletableFuture[]::new);

//        component.getComponentList().stream().forEach(x -> x.applyFunction((new LoadFromDatabase(widthQuery, heightQuery, colorQuery))::analysis));
////
//
//
//        if (component instanceof DataFile) {
//            return computeMetricsOfComponent((DataFile) component);
//        } else {
//            return 1;
//        }

        component.getComponentList().stream().map(
                (x) -> CompletableFuture.runAsync(()->x.applyFunction((new LoadFromDatabase(widthQuery,heightQuery,colorQuery))::analysis)))
                .map(x->x.join())
                .collect(Collectors.toList());

        if (component instanceof DataFile) {
            computeMetricsOfComponent((DataFile) component);
        }
        return 1;

    }


    /***
     *
     * @param dataFile set the component feature by a given query,Note: set only the matrix value no the actual size
     */
    private int computeMetricsOfComponent(DataFile dataFile) {
       Logger.info("Load From Data => "+ dataFile.getFeatures().getPath()+" Thread id=>"+Thread.currentThread().getId());

        String fn = dataFile.getFeatures().getPath().substring(dataFile.getFeatures().getPath().lastIndexOf(".") + 1);


        if (fn.indexOf("java") == 0) {

            String currentPath = dataFile.getFeatures().getPath();
            int width = (int) widthQuery.apply(currentPath).intValue();
            dataFile.getFeatures().setWidthMetrics(width);
            dataFile.getFeatures().setDepthMetrics(width);
            dataFile.getFeatures().setHeightMetrics(heightQuery.apply(currentPath).floatValue());
            long color = colorQuery.apply(currentPath);
            dataFile.getFeatures().setColorMetrics(color);

        }
        return 1;
    }
}

