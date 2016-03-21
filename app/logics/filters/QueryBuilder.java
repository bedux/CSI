package logics.filters;

import com.avaje.ebean.Expr;
import com.avaje.ebean.ExpressionList;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import interfaces.Filter;
import logics.models.db.ComponentInfo;
import logics.models.tools.Data;
import play.libs.Json;

import java.beans.Expression;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bedux on 10/03/16.
 */
public class QueryBuilder {


    public static ArrayList<Filter<ComponentInfo>> filters =new ArrayList<Filter<ComponentInfo>>()
                        {{
                            add(new PathName());
                            add(new NumberOfMethods());
                            add(new NumberOfWords());
                            add(new NumberOfLine());
                        }};




    public static Data QueryBuilder(String s){
        Data d = new Data();
        String[] str = s.split("&");
        for(String el:str){
            String[] element = el.split("=");
            if(element[0].contains("id")) {
                if (element.length > 1) {
                    d.id = Long.parseLong(element[1]);
                }
            }
            else if(element[0].contains("action")){
                if (element[1].equals("Show")){
                    d.actionList= Data.ActionList.SHOW;
                }else  if (element[1].equals("Hide")){
                    d.actionList= Data.ActionList.HIDE;
                }else  if (element[1].equals("HideOther")){
                    d.actionList= Data.ActionList.HIDEOTHER;
                }else  if (element[1].equals("ShowOther")){
                    d.actionList= Data.ActionList.SHOWOTHER;
                }
            }else if(element[0].contains("Color")){
                element[1]=element[1].replace("%23","#");
                d.color= element[1];
            }else{
                for(Filter<ComponentInfo> filter:filters){
                    if(filter.handleRequest(element,d)){
                        break;
                    }
                }
            }
        }
        return d;
    }

    public static JsonNode query(Data d){
        ExpressionList l = ComponentInfo.find.where().eq("repository.id", d.id);
        com.avaje.ebean.Expression expression = Expr.eq("repository.id", d.id);
        for(Filter<ComponentInfo> l1: filters){
            //Expr.a.add(l1.getExpressionFromData(d));
            expression = Expr.and(expression,l1.getExpressionFromData(d));
        }


        if(d.actionList== Data.ActionList.HIDEOTHER || d.actionList== Data.ActionList.SHOWOTHER){
            expression = Expr.not(expression);
        }
        l.add(expression);

        ObjectNode result = Json.newObject();
        result.put("data",Json.toJson(l.findList()));
        result.put("action",d.actionList.getValue());
        result.put("color",d.color);

        return result;

    }


    public static ObjectNode getFilters(Long id){


        ObjectNode result = Json.newObject();
        List<ObjectNode> lst = new ArrayList<>();
        for(Filter<ComponentInfo> l1: filters){
            lst.add(l1.getDescription(id));
        }
        result.put("filter",Json.toJson(lst));
        return result;

    }

}
