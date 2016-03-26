package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import interfaces.DataAttributes;
import logics.DatabaseManager;
import logics.Definitions;

import logics.databaseUtilities.SaveClassAsTable;
import logics.models.db.ComponentInfo;
import logics.models.db.JavaFile;
import logics.models.db.JavaFileInformation;
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
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Application extends Controller {

    public static Result index() {
        return getRepositories();
    }


    public static Result indexGet(int id){
//        try {
//            RepositoryVersion.find.where().eq("id",id).findList().get(0);
//            new AnalyserHandler().process(new AnalyserHandlerParam(RepositoryVersion.find.where().eq("id", id).findList().get(0)));
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        return ok("Done!");

    }


    public static Result getPrecomputedUrl(int id){
//        RepositoryVersion result = RepositoryVersion.find.byId(id);
//        System.out.println(id);
//        if(result!=null&&result.json!=null) {
//            return ok(result.json);
//        }else{
            return ok("Not Found");
//        }

    }

    public static Result getRepositories(){
//        SaveClassAsTable sast = new SaveClassAsTable();
//        JavaFile js = new JavaFile();
//        js.json = new JavaFileInformation();
//        js.name="marco";
//        js.id = 1;
//        js.path = "asdasd/asd/asd/as/da/sd";
//
//        try {
//            sast.save(js);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        }


        try {
            RepositoryVersion repositoryVersion = DatabaseManager.getInstance().getRepositoryVersionById(1);
            new AnalyserHandler().process(new AnalyserHandlerParam(repositoryVersion));

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return ok();
    }




    public static Result renderRepo(String id,int version) {
        System.out.println(id);
        Map<String,String> info = new HashMap<>();
////        String name = RepositoryVersion.find.byId(version).repository.uri;
//        info.put("name", name);
//        info.put("nol", ((Integer) (ComponentInfo.find.where().eq("repository.id", version).findList().stream().mapToInt(x -> x.getNoLine()).sum())).toString());
//        info.put("nod", ((Integer) (ComponentInfo.find.where().eq("repository.id", version).findList().size())).toString());
//        try {
//           Long size = Files.walk(Paths.get(Definitions.repositoryPath+version)).filter(p -> p.toFile().isFile()).mapToLong(p -> p.toFile().length()).sum();
//
//            info.put("size", Long.toString(size / 1024L)+" Kb");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


//        return ok(render.render(id,version,(QueryBuilder.getFilters(version)), DataFeatures.getMapMethod,info));
        return ok();
    }

    public static Result applyFilter() {
        Http.RequestBody body = request().body();
        String data = body.asText();
        Data d = QueryBuilder.QueryBuilder(data);
        JsonNode result =(QueryBuilder.query(d));
        return ok(Json.stringify(result));
    }

    public static Result getFilters(int id) {

       // return ok(Json.stringify(QueryBuilder.getFilters(id)));
        return ok();
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
