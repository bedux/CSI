package logics.filters;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebeaninternal.util.DefaultExpressionList;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import interfaces.Filter;
import logics.models.db.ComponentInfo;
import logics.models.tools.Data;
import play.libs.Json;

import javax.management.Query;
import java.util.List;

/**
 * Created by bedux on 10/03/16.
 */
public class NumberOfMethods implements Filter<ComponentInfo> {



    @Override
    public ExpressionList<ComponentInfo> getExpressionFromData(Data d) {
        return ComponentInfo.find.where().between("NOM", d.minNOM, d.maxNOM);
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

}
