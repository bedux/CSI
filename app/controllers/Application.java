package controllers;

import logics.models.db.ComponentInfo;
import logics.models.db.RepositoryVersion;
import logics.models.tools.Data;
import logics.filters.QueryBuilder;
import logics.pipeline.analayser.AnaliserHandler;
import logics.pipeline.analayser.AnalyserHandlerParam;
import play.libs.Json;
import play.mvc.*;

import com.fasterxml.jackson.databind.node.*;
import views.html.*;

public class Application extends Controller {

    public static Result index() {


        return ok(listRepository.render(RepositoryVersion.find.all()));
    }


    public static Result indexGet(){
        ObjectNode result = Json.newObject();
        result.put("dta", new AnaliserHandler().process(new AnalyserHandlerParam(RepositoryVersion.find.byId(1L))).json);
        return ok(result);

    }


    public static Result getPrecomputedUrl(long id){
        RepositoryVersion result = RepositoryVersion.find.byId(id);
        System.out.println(id);
        if(result!=null&&result.json!=null) {
            return ok(result.json);
        }else{
            return ok("Not Found");
        }

    }

    public static Result getRepositories(){
        getAllComponentMatch(1L,"analyzer");

        return ok(listRepository.render(RepositoryVersion.find.all()));
    }


    public static Result getAllComponentMatch(Long id,String match){

        return ok(Json.stringify(Json.toJson(ComponentInfo.find.where().eq("repository.id",id).contains("fileName",match).findList())));
    }

    public static Result renderRepo(String id,Long version) {
        return ok(render.render(id,version,(QueryBuilder.getFilters(version))));
    }

    public static Result applyFilter() {
        Http.RequestBody body = request().body();
        String data = body.asText();
        Data d = QueryBuilder.QueryBuilder(data);
        return ok(Json.stringify(QueryBuilder.query(d)));
    }

    public static Result getFilters(Long id) {

        return ok(Json.stringify(QueryBuilder.getFilters(id)));
    }
}
