package controllers;

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
        new AnaliserHandler().process(new AnalyserHandlerParam(RepositoryVersion.find.byId(1L)));
        return ok(render.render());
    }


    public static Result indexGet(){
        ObjectNode result = Json.newObject();
        result.put("dta", new AnaliserHandler().process(new AnalyserHandlerParam(RepositoryVersion.find.byId(1L))).json);
        return ok(result);

    }





}
