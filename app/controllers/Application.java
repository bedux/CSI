package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import interfaces.DataAttributes;
import logics.Definitions;
import logics.analyzer.DataFeatures;

import logics.models.db.ComponentInfo;
import logics.models.db.RepositoryVersion;
import logics.models.tools.Data;
import logics.filters.QueryBuilder;
import logics.pipeline.analayser.AnalyserHandler;
import logics.pipeline.analayser.AnalyserHandlerParam;
import play.libs.Json;
import play.mvc.*;
import views.html.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Application extends Controller {

    public static Result index() {
        return getRepositories();
    }


    public static Result indexGet(Long id){
        try {
            System.out.print(id);
            RepositoryVersion.find.where().eq("id",id).findList().get(0);
            new AnalyserHandler().process(new AnalyserHandlerParam(RepositoryVersion.find.where().eq("id", id).findList().get(0)));
        }catch (Exception e){
            e.printStackTrace();
        }
        return ok("Done!");

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




    public static Result renderRepo(String id,Long version) {
        System.out.println(id);
        Map<String,String> info = new HashMap<>();
        String name = RepositoryVersion.find.byId(version).repository.uri;
        info.put("name", name);
        info.put("nol", ((Integer) (ComponentInfo.find.where().eq("repository.id", version).findList().stream().mapToInt(x -> x.getNoLine()).sum())).toString());
        info.put("nod", ((Integer) (ComponentInfo.find.where().eq("repository.id", version).findList().size())).toString());
        try {
           Long size = Files.walk(Paths.get(Definitions.repositoryPath+version)).filter(p -> p.toFile().isFile()).mapToLong(p -> p.toFile().length()).sum();

            info.put("size", Long.toString(size / 1024L)+" Kb");
        } catch (IOException e) {
            e.printStackTrace();
        }


        return ok(render.render(id,version,(QueryBuilder.getFilters(version)), DataFeatures.getMapMethod,info));
    }

    public static Result applyFilter() {
        Http.RequestBody body = request().body();
        String data = body.asText();
        Data d = QueryBuilder.QueryBuilder(data);
        JsonNode result =(QueryBuilder.query(d));
        return ok(Json.stringify(result));
    }

    public static Result getFilters(Long id) {

        return ok(Json.stringify(QueryBuilder.getFilters(id)));
    }

    public static Result fileContent(String path) {
        path = Definitions.repositoryPath+ path.replaceAll("%2F","/");
        byte[] encoded = new byte[0];
        try {
            if(path.indexOf(".java")==path.length()-5) {
                encoded = Files.readAllBytes(Paths.get(path));
                String result = new String(encoded);
                return ok(result);
            }else{
                System.out.println("no Java File");
                return ok();
            }

        } catch (IOException e) {
            return ok();

        }
    }


    public static Result getStatistics(String id){
        id = id.replaceAll("%2F","/");
        return ok(Json.stringify(Json.toJson(ComponentInfo.find.where().eq("fileName", id).findList())));
    }

    static String generateStringSelect(String old,String add){
        return old + ", "+ add;
    }
}
