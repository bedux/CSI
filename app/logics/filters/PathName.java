package logics.filters;

import com.avaje.ebean.Expr;
import com.avaje.ebean.Expression;
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
public class PathName extends Filter<ComponentInfo> {



    @Override
    public Expression getExpressionFromData(Data d) {
        if(d.path.length()>0) {
            d.path=d.path.replaceAll("%2F","/");
            return Expr.contains("fileName", d.path);
        }
        else
            return Expr.isNotNull("fileName");
    }

    @Override
    public ObjectNode getDescription(Long id){
        ObjectNode result = Json.newObject();
        result.put("Type", FilterType.STRING.toString());
        result.put("Name","by Path");
        result.put("Id","path");

        return result;
    }

    @Override
    public boolean handleRequest(String request[], Data d) {
        if(request[0].contains("path")){
            if(request.length>1) {
                d.path = request[1];
                return true;
            }
        }
        return false;
    }

}
