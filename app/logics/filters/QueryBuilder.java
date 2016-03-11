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
        System.out.println(s);
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
            }else if(element[0].contains("action")){

                if (element[1].equals("Show")){
                    d.actionList= Data.ActionList.SHOW;
                }else  if (element[1].equals("Hide")){
                    d.actionList= Data.ActionList.HIDE;
                }else  if (element[1].equals("HideOther")){
                    d.actionList= Data.ActionList.HIDEOTHER;
                }else  if (element[1].equals("ShowOther")){
                    d.actionList= Data.ActionList.SHOWOTHER;
                }
            }
        }
        return d;
    }

    public static JsonNode query(Data d){
        ExpressionList l = ComponentInfo.find.where().eq("repository.id", d.id);
        for(Filter<ComponentInfo> l1: filters){
            l.addAll(l1.getExpressionFromData(d));
        }

        ObjectNode result = Json.newObject();
        result.put("data",Json.toJson(l.findList()));
        result.put("action",d.actionList.getValue());
        return result;

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
