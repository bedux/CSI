package controllers;

import com.avaje.ebean.RawSql;
import interfaces.VersionedSystem;
import logics.analyzer.RepoAnalyzer;
import logics.models.db.Repository;
import logics.models.db.RepositoryVersion;
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
        return ok(listRepository.render(RepositoryVersion.find.all()));
    }


    public static Result renderRepo(String id) {
        return ok(render.render(id));
    }
}
