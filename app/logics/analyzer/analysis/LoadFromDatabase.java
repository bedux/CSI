package logics.analyzer.analysis;

import interfaces.Analyser;
import interfaces.Component;
import logics.analyzer.DataFile;
import logics.models.modelQuery.IQuery;
import play.Logger;

import java.util.concurrent.*;


/**
 * Analyser that retrieve information for each file
 */
public class LoadFromDatabase implements Analyser<Integer> {

    private final IQuery<String,Long> widthQuery;
    private final IQuery<String,Long> heightQuery;
    private final IQuery<String,Long> colorQuery;

    /***
     *
     * @param width the with query  used to get the metrics
     * @param height the with query  used to get the metrics
     * @param color the with query  used to get the metrics
     */
    public LoadFromDatabase(IQuery<String,Long> width, IQuery<String,Long> height, IQuery<String,Long> color) {
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
    public Integer analysis(Component component) {
//        CompletableFuture[] res =
//                component.getComponentList().stream().map(
//                        (x) -> CompletableFuture.supplyAsync(() -> x.applyFunction((new LoadFromDatabase(widthQuery.clone(), heightQuery.clone(), colorQuery.clone()))::analysis),ThreadManager.instance().getExecutor()
//                        )
//                ).toArray(CompletableFuture[]::new);

        component.getComponentList().stream().forEach(x -> x.applyFunction((new LoadFromDatabase(widthQuery.clone(), heightQuery.clone(), colorQuery.clone()))::analysis));
//


        if (component instanceof DataFile) {
            return computeMetricsOfComponent((DataFile) component);
        } else {
            return 1;
        }
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
            int width = (int) widthQuery.clone().execute(currentPath).intValue();
            dataFile.getFeatures().setWidthMetrics(width);
            dataFile.getFeatures().setDepthMetrics(width);
            dataFile.getFeatures().setHeightMetrics(heightQuery.clone().execute(currentPath).floatValue());
            long color = colorQuery.clone().execute(currentPath);
            dataFile.getFeatures().setColorMetrics(color);

        }
        return 1;
    }
}

