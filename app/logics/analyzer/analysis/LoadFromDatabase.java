package logics.analyzer.analysis;

import interfaces.Analyser;
import interfaces.Component;
import logics.analyzer.BinaryFile;
import logics.analyzer.DataFile;
import logics.models.query.IComputeAttributeContainer;

/**
 * Created by bedux on 26/03/16.
 */
public class LoadFromDatabase implements Analyser<Integer> {
    IComputeAttributeContainer widthQuery;
    IComputeAttributeContainer heightQuery;
    IComputeAttributeContainer colorQuery;

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
            c.getFeatures().setWidth(width);
            c.getFeatures().setDeep(width);
            c.getFeatures().setHeight(heightQuery.executeAndGetResult(currentPath));
            long color = colorQuery.executeAndGetResult(currentPath);
            c.getFeatures().setColor(color);
        }
    }
}

