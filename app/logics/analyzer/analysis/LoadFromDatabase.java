package logics.analyzer.analysis;

import interfaces.Analyser;
import interfaces.Component;
import logics.analyzer.BinaryFile;
import logics.analyzer.DataFile;
import logics.databaseUtilities.QueryContainer;
import logics.models.query.Count;
import logics.models.query.QueryWithPath;

import java.util.HashMap;

/**
 * Created by bedux on 26/03/16.
 */
public class LoadFromDatabase implements Analyser<Integer> {
    QueryWithPath widthQuery;QueryWithPath heightQuery;QueryWithPath colorQuery;
    public LoadFromDatabase(QueryWithPath width,QueryWithPath height,QueryWithPath color){
        this.widthQuery = width;
        this.heightQuery = height;
        this.colorQuery = color;
    }
    @Override
    public Integer analysis(Component value) {
        value.getComponentList().stream().forEach((x) -> x.applyFunction((new LoadFromDatabase(widthQuery,heightQuery,colorQuery))::analysis));
        if (value instanceof BinaryFile) {

        } else if (value instanceof DataFile) {
            analysisCast((DataFile) value);
        }
        return 1;
    }

    private void analysisCast(DataFile c) {
        String fn = c.getFeatures().getPath().substring(c.getFeatures().getPath().lastIndexOf(".") + 1);


        if (fn.indexOf("java") == 0) {

            widthQuery.setPath(c.getFeatures().getPath());
            heightQuery.setPath(c.getFeatures().getPath());
            colorQuery.setPath(c.getFeatures().getPath());

            int width =(int)  widthQuery.executeAndGetFirst().count;
            c.getFeatures().setWidth(width);
            c.getFeatures().setDeep(width);
            c.getFeatures().setHeight(heightQuery.executeAndGetFirst().count);
            c.getFeatures().setColor(colorQuery.executeAndGetFirst().count);
        }
    }
}

