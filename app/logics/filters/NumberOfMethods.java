package logics.filters;

import com.avaje.ebean.Expr;
import com.avaje.ebean.Expression;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebeaninternal.util.DefaultExpressionList;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import interfaces.Filter;
import logics.models.db.ComponentInfo;
import logics.models.tools.Data;
import play.libs.Json;

import com.avaje.ebean.ExpressionList;

import java.util.List;

/**
 * Created by bedux on 10/03/16.
 */
public class NumberOfMethods extends Filter<ComponentInfo> {



    @Override
    public Expression getExpressionFromData(Data d) {
        return Expr.between("NOM", d.minNOM, d.maxNOM);
    }

    @Override
    public  ObjectNode getDescription(Long id){
        ObjectNode result = Json.newObject();
        result.put("Type",FilterType.RANGE.toString());
        result.put("Name","by Number of method");
        result.put("Id","NOM");

        List<ComponentInfo> t = ComponentInfo.find.where().eq("repository.id", id).orderBy("NOM").findList();
        result.put("Min",t.get(0).NOM);
        result.put("Max",t.get(t.size()-1).NOM);
        return result;
    }

    @Override
    public boolean handleRequest(String request[], Data d) {
        if(request[0].contains("NOM")){
            String[] el2 =  Filter.parseRabge(request[1]);
            d.minNOM = Integer.parseInt(el2[0]);
            d.maxNOM = Integer.parseInt(el2[1]);
            return true;
        }
        return false;
    }

}