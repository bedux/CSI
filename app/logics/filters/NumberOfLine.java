package logics.filters;

import com.avaje.ebean.Expr;
import com.avaje.ebean.Expression;
import com.fasterxml.jackson.databind.node.ObjectNode;
import logics.models.db.ComponentInfo;
import interfaces.Filter;
import logics.models.tools.Data;
import play.libs.Json;

import java.util.List;

/**
 * Created by bedux on 15/03/16.
 */
public class NumberOfLine extends Filter<ComponentInfo> {

    @Override
     public Expression getExpressionFromData(Data d) {
        return Expr.between("NOL", d.minNOL, d.maxNOL);
    }

    @Override
    public ObjectNode getDescription(Long id) {
            ObjectNode result = Json.newObject();
            result.put("Type",FilterType.RANGE.toString());
            result.put("Name","by Number of Line");
            result.put("Id","NOL");

            List<ComponentInfo> t = ComponentInfo.find.where().eq("repository.id", id).orderBy("NOL").findList();
            result.put("Min",t.get(0).NOL);
            result.put("Max",t.get(t.size()-1).NOL);
            return result;
    }

    @Override
    public boolean handleRequest(String[] request, Data d) {
        if(request[0].contains("NOL")){
            String[] el2 =  Filter.parseRabge(request[1]);
            d.minNOL = Integer.parseInt(el2[0]);
            d.maxNOL = Integer.parseInt(el2[1]);
        }
        return false;
    }
}
