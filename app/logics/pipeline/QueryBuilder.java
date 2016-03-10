package logics.pipeline;

import logics.models.tools.Data;

/**
 * Created by bedux on 10/03/16.
 */
public class QueryBuilder {




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

    private static String[] parseRabge(String r){
        r=r.replace("+","");
       return  r.split("-");
    }
}
