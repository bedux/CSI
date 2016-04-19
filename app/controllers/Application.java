package controllers;

import exception.CustomException;
import exception.SQLnoResult;
import logics.Definitions;
import logics.models.db.*;
import logics.models.query.QueryGetAllRepository;
import logics.models.query.QueryList;
import logics.pipeline.PipelineManager;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.listRepository;
import views.html.render;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class Application extends Controller {

    public static Result index() {
        return getRepositories();
    }


    public static Result indexGet(int id) {
        RepositoryVersion repositoryVersion = null;
        try {
            repositoryVersion = QueryList.getInstance().getRepositoryVersionById(id).orElseThrow(()-> new SQLnoResult());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        new PipelineManager().StoreAndAnalyze(repositoryVersion);
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
        List<RepositoryRender> repositoryVersionList = new QueryGetAllRepository().execute();
        for(RepositoryRender v:repositoryVersionList){
            try {
                v.nameToDisplay = QueryList.getInstance().getProjectName(v.repositoryId);
                v.numberOfFileToDispaly = QueryList.getInstance().getNumberOfFile(v.repositoryId);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return ok(listRepository.render(repositoryVersionList));
    }


    public static Result renderRepo(String id, int version) {

        Map<String, String> info = new HashMap<>();
        try {
            info.put("size",Long.toString(QueryList.getInstance().getTotalSize(version)));
            info.put("nol",Long.toString(QueryList.getInstance().getTotalNumberOfCodeLines(version)));
            info.put("name",(QueryList.getInstance().getProjectName(version)));
            info.put("nod",Long.toString(QueryList.getInstance().getNumberOfFile(version)));


        } catch (SQLException e) {
          throw new CustomException(e);
        } catch (IOException e) {
            throw new CustomException(e);
        }

        Map<String, String> getMapMethod = new HashMap();
        getMapMethod.put("Method Count Width/Depth","depthMetrics");
        getMapMethod.put("Fields Count Height","heightMetrics");
        getMapMethod.put("Java Doc Percentage Color","colorMetrics");



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




    public static Result getDiscussion()  {
        try {
            String path = request().body().asJson().get("path").asText();
            JavaFile jf = QueryList.getInstance().getJavaFileByPath(path).orElseThrow(() -> new SQLnoResult());
            List<ImportDiscussion> javaImports = QueryList.getInstance().getAllDiscussionImport(QueryList.getInstance().getAllNonLocalImport(jf.id, jf.repositoryVersionId));

            final List<String> allJavaMethods  = QueryList.getInstance().getAllJavaMethodOfRepositoryVersion(jf.repositoryVersionId);
            final List<String> currentMethods =  QueryList.getInstance().getAllJavaMethodFormPath(path);
            final List<String> javaMethods =currentMethods.stream().distinct().filter(x -> !allJavaMethods.stream().anyMatch(y -> y.equals(x))).collect(Collectors.toList());
            javaMethods.forEach(System.out::println);
            final HashMap<String,List<String>> resul = new HashMap<>();

            javaMethods.stream().forEach(x -> {
                   try {
                       List<StackOFDiscussion> sovfd = QueryList.getInstance().getAllDiscussionHavingMethodName(new ArrayList<String>() {{
                           add(x);
                       }});
                       sovfd.forEach(z->{
                           if(resul.containsKey(x)){
                             resul.get(x).add(z.packageDiscussion);
                           }else{
                               resul.put(x,new ArrayList<String>(){{add(z.packageDiscussion);}});
                           }
                       });

                   } catch (SQLException e) {
                       e.printStackTrace();
                   }
            });

            javaImports.stream().forEach(x -> {
                try {
                    List<StackOFDiscussion> sovfd = QueryList.getInstance().getGitDiscussionFromImportDiscussion(new ArrayList<ImportDiscussion>() {{
                        add(x);
                    }});
                    sovfd.forEach(z->{
                        if(resul.containsKey(x)){
                            resul.get(x.packageDiscussion).add(z.packageDiscussion);
                        }else{
                            resul.put(x.packageDiscussion,new ArrayList<String>(){{add(z.packageDiscussion);}});
                        }
                    });

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            String res = Json.stringify(Json.toJson(resul));
            return ok(res);
//            System.out.println(res);
//
//
//
//            final List<StackOFDiscussion> discussionsReleatedToMethodName = QueryList.getInstance().getAllDiscussionHavingMethodName(javaMethods);
//
//
//            final List<StackOFDiscussion>result = QueryList.getInstance().getGitDiscussionFromImportDiscussion(javaImports);
//
//            Logger.info("__________________");
//
//            javaImports.stream().map(x->x.packageDiscussion).forEach(Logger::info);
//
//            result.addAll(discussionsReleatedToMethodName);
//            String res1 = Json.stringify(Json.toJson(result.stream().map(x->x.packageDiscussion).distinct().collect(Collectors.toList())));
//            return ok(res1);
        }catch (SQLException e){
            throw new CustomException(e);
        }
        catch (Exception e){
            return ok("Error");
        }


    }
}
