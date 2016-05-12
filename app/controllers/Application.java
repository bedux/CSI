package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import exception.CustomException;
import exception.SQLnoResult;
import interfaces.Filter;
import logics.Definitions;
import logics.analyzer.analysis.ThreadManager;
import logics.databaseCache.DatabaseModels;
import logics.databaseUtilities.Pair;
import logics.filters.QueryBuilder;
import logics.models.db.*;
import logics.models.modelQuery.*;
import logics.models.query.QueryList;
import logics.models.tools.Data;
import logics.pipeline.PipelineManager;
import logics.pipeline.analayser.AnalyserHandler;
import logics.pipeline.analayser.AnalyserHandlerParam;
import logics.pipeline.analayser.MetricsCharacteristics;
import logics.pipeline.tree.TreeGenerator;
import logics.pipeline.tree.TreeGeneratorHandleParam;
import logics.pipeline.tree.TreeGeneratorHandlerResult;
import logics.versionUtils.GitRepo;
import logics.versionUtils.VersionCommit;
import play.Play;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.listRepository;
import views.html.loading;
import views.html.render;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Application extends Controller {


    public static Result myAsset(String file){
        String s = Play.application().path().getAbsolutePath()+"/asset/"+file;
        return ok(new java.io.File(s));

    }

    private static HashMap<String,Function<String,Long>> queryAvailable = new HashMap<String,Function<String,Long>>(){{
        put("FieldCount",Query::CountFieldByPath);
        put("MethodsCount",Query::CountMethodByPath);
        put("DiscussionCount",Query::DiscussedImportMethodCounter);
        put("JavaDoc",Query::JavaDocForMethodCount);
        put("ClassCount",Query::CountJavaClass);
        put("InterfaceCount",Query::CountJavaInterface);

    }};

    public static Result index() {
        return getRepositories();
    }


    public static Result indexGet(int id) {
        Long ids = (long)id;
        Optional<RepositoryVersion> repositoryVersion = Query.byId(new Pair<>(ids, RepositoryVersion.class));

        repositoryVersion.orElseThrow(()-> new SQLnoResult());

        Long idRun = new PipelineManager().StoreAndAnalyze(repositoryVersion.get());
        return ok(loading.render(idRun));



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


    public static Result customComputation(){
        Long repoId  = request().body().asJson().get("repositoryId").asLong();
        String widthQ  = request().body().asJson().get("width").asText();
        String heightQ  =request().body().asJson().get("height").asText();
        String colorQ  =request().body().asJson().get("color").asText();


        RepositoryVersion repo = Query.byId(new Pair<>(repoId, RepositoryVersion.class)).get();
        TreeGeneratorHandlerResult treeForG = new TreeGenerator().process(new TreeGeneratorHandleParam(repo));
        String uuid = repoId +widthQ + heightQ +colorQ;
        Optional<RepositoryRender> repoRender = Query.All(RepositoryRender.class).stream().filter(x->x.getMetricType().equals(uuid)).findAny();
        if(repoRender.isPresent()){
            return  ok(new java.io.File( Play.application().path().getAbsolutePath()+repoRender.get().getLocalPath()));
        }
        if(!queryAvailable.containsKey(widthQ)){
            System.out.println(widthQ + " Che è??");
            return ok();
        }
        if(!queryAvailable.containsKey(heightQ)){
            System.out.println(widthQ+" Che è??");
            return ok();
        }
        if(!queryAvailable.containsKey(colorQ)){
            System.out.println(widthQ+" Che è??");
            return ok();
        }
        MetricsCharacteristics ms6 = new MetricsCharacteristics(queryAvailable.get(widthQ), queryAvailable.get(heightQ),queryAvailable.get(colorQ),uuid);
        AnalyserHandlerParam result6 = new AnalyserHandlerParam(repo,treeForG.root,ms6);
//        result6.percentage = false;
//        result6.isOnlyPackage = true;
        JsonNode ca  = new AnalyserHandler().process(result6).json;
        Result res = ok(Json.stringify(ca));
        return res;
    }

    public static Result getRepositories() {
        List<RepositoryRender> repositoryVersionList = Query.All(RepositoryRender.class);
        if(repositoryVersionList.size()<=1){
            return ok(listRepository.render(new ArrayList<>()));
        }
        for(RepositoryRender v:repositoryVersionList){

                v.setNameToDisplay(   Query.ProjectName(v.getRepositoryVersion().getId()));
                v.setNumberOfFileToDispaly(  Query.NumberOfFile(v.getRepositoryVersion().getId()));

        }

        return ok(listRepository.render( repositoryVersionList.stream().filter(x -> x.getMetricType().contentEquals("Discussion and import percentage")).collect(Collectors.toList())));
    }


    public static Result renderRepo(String id, Long version) {

        Map<String, String> info = new HashMap<>();

            info.put("size",Long.toString(Query.TotalSize(version)));
            info.put("nol",Long.toString(Query.TotalNumberOfCodeLines(version)));
            info.put("name",(Query.ProjectName(version)));
            info.put("nod",Long.toString(Query.NumberOfFile(version)));



        Map<String, String> getMapMethod = new HashMap();
        getMapMethod.put("Height","depthMetrics");
        getMapMethod.put("Width/Depth","heightMetrics");
        getMapMethod.put("Color","colorMetrics");



        return ok(render.render(id, (long) version, QueryBuilder.getFilters(version), getMapMethod, info));
//        return ok();
    }

    public static Result getAllRepositoryRender(Long repositoryVersion){
       RepositoryVersion repo =  Query.byId(new Pair<>(repositoryVersion, RepositoryVersion.class)).orElseThrow(()-> new CustomException("T"));
        List<RepositoryRender> repositoryRenders = repo.getListOfRepositoryRender();
        System.out.println(repositoryRenders.size());
        class ResultType{
            public String url;
            public String type;
        }
        return ok(Json.stringify(Json.toJson(repo.getListOfRepositoryRender().stream()
                .map(x->{
            ResultType r = new ResultType();
            r.type = x.getMetricType();
            r.url = x.getLocalPath();
            return r;
        })
        .collect(Collectors.toList()))));
    }

    public static Result applyFilter() {
        Http.RequestBody body = request().body();
        String data = body.asText();
        Data d = QueryBuilder.QueryBuilder(data);
        JsonNode result =(QueryBuilder.query(d));
        return ok();
    }

    public static Result getFilters(int id) {

        // return ok(Json.stringify(QueryBuilder.getFilters(id)));
        return ok();
    }

    public static Result fileContent(String path) {
        path = Definitions.repositoryPath + path.replaceAll("%2F", "/");
        path = Play.application().getFile(path).getAbsolutePath();
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
        return ok();
    }

    static String generateStringSelect(String old, String add) {
        return old + ", " + add;
    }




    public static Result getDiscussion()  {
//


        final String path = request().body().asJson().get("path").asText();
        final HashMap<String,List<String>> resul = new HashMap<>();
        Query.AllDiscussedMethod(path)
                .stream()
                .map(x -> new Pair<String,List<String>>(x.getMethodName(), x.getListOfMethodDiscussion().stream().map(y -> y.getDiscussionConcrete().getDiscussionURL()).collect(Collectors.toList())))
                .forEach(x->{
                    if(resul.containsKey(x.getKey())){
                        resul.get(x.getKey()).addAll(x.getValue());
                    }else{
                        resul.put(x.getKey(),x.getValue());
                    }
                });
        Query.AllDiscussedImport(path)
                .stream()
                .map(x -> new Pair<String,List<String>>(x.getPackageDiscussion(), x.getListOfImportDiscussion().stream().map(y -> y.getDiscussionConcrete().getDiscussionURL()).collect(Collectors.toList())))
                .forEach(x -> {
                    if (resul.containsKey(x.getKey())) {
                        resul.get(x.getKey()).addAll(x.getValue());
                    } else {
                        resul.put(x.getKey(), x.getValue());
                    }
                });
                return ok(Json.stringify(Json.toJson(resul)));


    }
}
