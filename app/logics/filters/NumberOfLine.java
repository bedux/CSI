//package logics.filters;

/**
 * Created by bedux on 15/03/16.
 */
//public class NumberOfLine extends Filter<ComponentInfo> {
//
//    @Override
//     public Expression getExpressionFromData(Data d) {
//        return Expr.between(DataName.NoLine.getValue(), d.minNOL, d.maxNOL);
//    }
//
//    @Override
//    public ObjectNode getDescription(Long id) {
//
//            ObjectNode result = Json.newObject();
//            result.put("Type",FilterType.RANGE.toString());
//            result.put("Name","by Number of Line");
//            result.put("Id",DataName.NoLine.getValue());
//
//            List<ComponentInfo> t = ComponentInfo.find.where().eq("repository.id", id).orderBy(DataName.NoLine.getValue()).findList();
//            result.put("Min",t.get(0).getNoLine());
//            result.put("Max",t.get(t.size()-1).getNoLine());
//
//        return result;
//    }
//
//    @Override
//    public boolean handleRequest(String[] request, Data d) {
//        if(request[0].contains(DataName.NoLine.getValue())){
//            String[] el2 =  Filter.parseRabge(request[1]);
//            d.minNOL = Integer.parseInt(el2[0]);
//            d.maxNOL = Integer.parseInt(el2[1]);
//        }
//        return false;
//    }
//}
