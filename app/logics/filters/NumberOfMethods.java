//package logics.filters;
//
//import com.avaje.ebean.Expr;
//import com.avaje.ebean.Expression;
//
//import com.fasterxml.jackson.databind.node.ObjectNode;
//import interfaces.DataAttributes;
//import interfaces.Filter;
//import logics.databaseUtilities.Pair;
//import logics.models.db.JavaFile;
//import logics.models.db.JavaMethod;
//import logics.models.db.RepositoryRender;
//import logics.models.db.RepositoryVersion;
//import logics.models.modelQuery.Query;
//import logics.models.tools.Data;
//import play.libs.Json;
//import interfaces.DataAttributes.*;
//
//import javax.swing.text.html.parser.Entity;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
///**
// * Created by bedux on 10/03/16.
// */
//public class NumberOfMethods extends Filter {
//
//
//
//    @Override
//    public String getExpressionFromData(Data d) {
//
//        return "";
//    }
//
//    @Override
//    public  ObjectNode getDescription(Long id){
//        ObjectNode result = Json.newObject();
//        result.put("Type",FilterType.RANGE.toString());
//        result.put("Name","by Number of method");
//        result.put("Id",DataName.NoMethod.toString());
//
//        result.put("Min",0);
//
//       RepositoryVersion r =  Query.byId(new Pair<Long, Class<RepositoryVersion>>(id, RepositoryVersion.class)).get();
//        List<Long> l = r.getListOfFile().stream().map(y -> y.getId()).collect(Collectors.toList());
//        Map<?,List<JavaMethod>> mamma= Query.All(JavaMethod.class)
//                                .stream()
//                                .filter(x -> l.contains(x.getJavaClassConcrete().getJavaFileConcrete().getId()))
//                                .collect(Collectors.groupingBy(x->x.getJavaClassConcrete().getJavaFileConcrete().getId()));
//        int val = mamma.values().stream().map(x->x.size()).max(Long::compare).get();
//        result.put("Max",val);
//
////        List<ComponentInfo> t = ComponentInfo.find.where().eq("repository.id", id).orderBy(DataName.NoMethod.toString()).findList();
////        result.put("Min",t.get(0).getNoMethod());
////        result.put("Max",t.get(t.size()-1).getNoMethod());
//        return result;
//    }
//
//    @Override
//    public boolean handleRequest(String request[], Data d) {
//        if(request[0].contains(DataName.NoMethod.toString())){
//            String[] el2 =  Filter.parseRabge(request[1]);
//            d.minNOM = Integer.parseInt(el2[0]);
//            d.maxNOM = Integer.parseInt(el2[1]);
//            return true;
//        }
//        return false;
//    }
//
//}
