package controllers;

import logics.Definitions;
import logics.models.db.RepositoryVersion;
import logics.models.query.QueryGetAllRepository;
import logics.models.query.QueryList;
import logics.pipeline.analayser.AnalyserHandler;
import logics.pipeline.analayser.AnalyserHandlerParam;
import logics.pipeline.storing.StoreHandler;
import logics.pipeline.storing.StoreHandlerParam;
import logics.pipeline.storing.StoreHandlerResult;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.listRepository;
import views.html.render;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Application extends Controller {

    public static Result index() {
        return getRepositories();
    }


    public static Result indexGet(int id) {
//        try {
//            RepositoryVersion.find.where().eq("id",id).findList().get(0);
//            new AnalyserHandler().process(new AnalyserHandlerParam(RepositoryVersion.find.where().eq("id", id).findList().get(0)));
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        return ok("Done!");

    }


    public static Result getPrecomputedUrl(int id) {
//        RepositoryVersion result = RepositoryVersion.find.byId(id);
//        System.out.println(id);
//        if(result!=null&&result.json!=null) {
//            return ok(result.json);
//        }else{
        return ok("Not Found");
//        }

    }

    public static Result getRepositories() {
        RepositoryVersion repositoryVersion = null;
        try {
            repositoryVersion = QueryList.getInstance().getRepositoryVersionById(1);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        new AnalyserHandler().process(new AnalyserHandlerParam((repositoryVersion)));

        return ok(listRepository.render(new QueryGetAllRepository().execute()));
    }


    public static Result renderRepo(String id, int version) {
        System.out.println(id);
        Map<String, String> info = new HashMap<>();
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
        Map<String, String> getMapMethod = new HashMap();


        return ok(render.render(id, (long) version, null, getMapMethod, info));
//        return ok();
    }

    public static Result applyFilter() {
//        Http.RequestBody body = request().body();
//        String data = body.asText();
//        Data d = QueryBuilder.QueryBuilder(data);
//        JsonNode result =(QueryBuilder.query(d));
        return ok();
    }

    public static Result getFilters(int id) {

        // return ok(Json.stringify(QueryBuilder.getFilters(id)));
        return ok();
    }

    public static Result fileContent(String path) {
        path = Definitions.repositoryPath + path.replaceAll("%2F", "/");
        byte[] encoded = new byte[0];
        try {
            if (path.indexOf(".java") == path.length() - 5) {
                encoded = Files.readAllBytes(Paths.get(path));
                String result = new String(encoded);
                return ok(result);
            } else {
                System.out.println("no Java File");
                return ok();
            }

        } catch (IOException e) {
            return ok();

        }
    }


    public static Result getStatistics(String id) {
        id = id.replaceAll("%2F", "/");
//        return ok(Json.stringify(Json.toJson(ComponentInfo.find.where().eq("fileName", id).findList())));
        return ok();
    }

    static String generateStringSelect(String old, String add) {
        return old + ", " + add;
    }
}
