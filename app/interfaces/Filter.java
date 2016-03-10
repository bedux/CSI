package interfaces;

import com.avaje.ebean.ExpressionList;
import com.fasterxml.jackson.databind.node.ObjectNode;
import logics.models.tools.Data;
import play.libs.Json;

/**
 * Created by bedux on 10/03/16.
 */
public interface Filter<T> {
     enum  FilterType {RANGE("RANGE"),STRING("INPUT");
         private String value;

         FilterType(final String value) {
             this.value = value;
         }

         public String getValue() {
             return value;
         }

         @Override
         public String toString() {
             return this.getValue();
         }
     }

    ExpressionList<T> getExpressionFromData(Data d);

     ObjectNode getDescription(Long id);

     static String[] parseRabge(String r){
        r=r.replace("+","");
        return  r.split("-");
    }

    }
