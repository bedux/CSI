package logics.filters;

import com.avaje.ebean.ExpressionList;
import com.fasterxml.jackson.databind.node.ObjectNode;
import interfaces.Filter;
import logics.models.db.ComponentInfo;
import logics.models.tools.Data;
import play.libs.Json;

import java.util.List;

/**
 * Created by bedux on 10/03/16.
 */
public class NumberOfWords implements Filter<ComponentInfo> {


    @Override
    public ExpressionList<ComponentInfo> getExpressionFromData(Data d) {
        return ComponentInfo.find.where().between("WC", d.minWC, d.maxWC);
    }

    @Override
    public ObjectNode getDescription(Long id){
        ObjectNode result = Json.newObject();
        result.put("Type",FilterType.RANGE.toString());
        result.put("Name","by Word Count");
        result.put("Id","WC");



        List<ComponentInfo> t = ComponentInfo.find.where().eq("repository.id", id).orderBy("WC").findList();
        result.put("Min",t.get(0).WC);
        result.put("Max",t.get(t.size()-1).WC);
        return result;
    }

}