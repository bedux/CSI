package logics.filters;

import com.avaje.ebean.Expr;
import com.avaje.ebean.Expression;
import com.avaje.ebean.ExpressionList;
import com.fasterxml.jackson.databind.node.ObjectNode;
import interfaces.DataAttributes;
import interfaces.Filter;
import logics.models.db.ComponentInfo;
import logics.models.tools.Data;
import play.libs.Json;
import interfaces.DataAttributes.*;


import java.util.List;

/**
 * Created by bedux on 10/03/16.
 */
public class NumberOfWords extends Filter<ComponentInfo> {


    @Override
    public Expression getExpressionFromData(Data d) {
        return Expr.between(DataName.NoWord.getValue(), d.minWC, d.maxWC);
    }

    @Override
    public ObjectNode getDescription(Long id){
        ObjectNode result = Json.newObject();
        result.put("Type",FilterType.RANGE.toString());
        result.put("Name","by Word Count");
        result.put("Id", DataName.NoWord.getValue());



        List<ComponentInfo> t = ComponentInfo.find.where().eq("repository.id", id).orderBy(DataName.NoWord.getValue()).findList();
        result.put("Min",t.get(0).getNoWord());
        result.put("Max",t.get(t.size()-1).getNoWord());
        System.out.println(Json.stringify(result));

        return result;
    }

    @Override
    public boolean handleRequest(String[] request, Data d) {
        if(request[0].contains(DataName.NoWord.getValue())){
            String[] el2 =  Filter.parseRabge(request[1]);
            d.minWC = Integer.parseInt(el2[0]);
            d.maxWC = Integer.parseInt(el2[1]);
            return true;
        }
        return false;
    }

}