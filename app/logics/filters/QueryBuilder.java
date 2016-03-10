package logics.filters;

import com.avaje.ebean.ExpressionList;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import interfaces.Filter;
import logics.models.db.ComponentInfo;
import logics.models.tools.Data;
import play.libs.Json;

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
                        }};




    public static Data QueryBuilder(String s){
        Data d = new Data();
        String[] str = s.split("&");
        for(String el:str){
            String[] element = el.split("=");
            if(element[0].contains("path")){
                if(element.length>1) {
                    d.path = element[1];
                }
            }
            else if(element[0].contains("id")){
                if(element.length>1) {
                    d.id = Long.parseLong(element[1]);
                }
            }else  if(element[0].contains("NOM")){
                String[] el2 =  parseRabge(element[1]);
                d.minNOM = Integer.parseInt(el2[0]);
                d.maxNOM = Integer.parseInt(el2[1]);
            }else  if(element[0].contains("WC")){
                String[] el2 =  parseRabge(element[1]);
                d.minWC = Integer.parseInt(el2[0]);
                d.maxWC = Integer.parseInt(el2[1]);
            }else  if(element[0].contains("NOL")){
                String[] el2 =  parseRabge(element[1]);
                d.minNOL = Integer.parseInt(el2[0]);
                d.maxNOL = Integer.parseInt(el2[1]);
            }
        }
        return d;
    }

    public static JsonNode query(Data d){
        ExpressionList l = ComponentInfo.find.where().eq("repository.id", d.id);
        for(Filter<ComponentInfo> l1: filters){
            l.addAll(l1.getExpressionFromData(d));
        }
        return Json.toJson(l.findList());

    }

    private static String[] parseRabge(String r){
        r=r.replace("+","");
       return  r.split("-");
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
