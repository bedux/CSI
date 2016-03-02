package controllers;

import logics.analyzer.RepoAnalyzer;
import logics.models.db.Repo;
import play.libs.Json;
import play.mvc.*;

import com.fasterxml.jackson.databind.node.*;
import views.html.*;

public class Application extends Controller {

    public static Result index() {
        return ok(render.render());
    }


    public static Result indexGet(){
        ObjectNode result = Json.newObject();

        RepoAnalyzer repoAnalyzer = new RepoAnalyzer(Repo.find.all().get(0));
        result.put("dta",repoAnalyzer.getTree());
        return ok(result);

    }





}
