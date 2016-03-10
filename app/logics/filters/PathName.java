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
public class PathName implements Filter<ComponentInfo> {



    @Override
    public ExpressionList<ComponentInfo> getExpressionFromData(Data d) {
        if(d.path.length()>0) {
            d.path=d.path.replaceAll("%2F","/");
            System.out.println(d.path);

            return ComponentInfo.find.where().contains("fileName", d.path);

        }
        else
            return ComponentInfo.find.where();
    }

    @Override
    public ObjectNode getDescription(Long id){
        ObjectNode result = Json.newObject();
        result.put("Type", FilterType.STRING.toString());
        result.put("Name","by Path");
        result.put("Id","path");

        return result;
    }

}
