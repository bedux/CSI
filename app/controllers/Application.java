package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import exception.CustomException;
import exception.SQLnoResult;
import logics.Definitions;
import logics.databaseUtilities.Pair;
import logics.filters.QueryBuilder;
import logics.models.modelQuery.Query;
import logics.models.newDatabase.*;
import logics.models.tools.Data;
import logics.pipeline.PipelineManager;
import logics.pipeline.analayser.AnalyserHandler;
import logics.pipeline.analayser.AnalyserHandlerParam;
import logics.pipeline.analayser.MetricsCharacteristics;
import logics.pipeline.tree.TreeGenerator;
import logics.pipeline.tree.TreeGeneratorHandleParam;
import logics.pipeline.tree.TreeGeneratorHandlerResult;
import play.Play;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.listRepository;
import views.html.loading;
import views.html.render;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Application extends Controller {


    public static Result myAsset(String file){
        String s = Play.application().path().getAbsolutePath()+"/asset/"+file;
        return ok(new java.io.File(s));

    }

    private static HashMap<String,Supplier<Function<String, Long>> >  queryAvailable =
            new HashMap<String,Supplier<Function<String, Long>>>(){{


        put("FieldCount", ()->new Query().CountFieldByPathWrap);
        put("MethodsCount",()->new Query().CountMethodByPathWrap);
        put("DiscussionCount",()->new Query().DiscussedImportMethodCounterWrap);
                put("DiscussionCountPerc",()->new Query().DiscussedImportMethodCounterOverTotalWrap);
        put("JavaDoc",()->new Query().JavaDocForMethodCountWrap);
                put("JavaDocPerc",()->new Query().JavaDocForMethodCountWrap);

                put("ClassCount",()->new Query()::CountJavaClass);
        put("InterfaceCount",()->new Query()::CountJavaInterface);

    }};

    public static Result index() {
        return getRepositories();
    }


    private static String getName(JavaImport ji){
        return ji.packageName;
    }

    public static Result indexGet(int id) {


        Query q = new Query();
        Ebean.getServerCacheManager().clearAll();

        Long ids = (long)id;

        Optional<RepositoryVersion> repositoryVersion = q.<RepositoryVersion>byId(new Pair<>(ids, RepositoryVersion.find));

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

        Query q = new Query();
        RepositoryVersion repo = q.<RepositoryVersion>byId(new Pair<>(repoId, RepositoryVersion.find)).get();
        TreeGeneratorHandlerResult treeForG = new TreeGenerator().process(new TreeGeneratorHandleParam(repo));
        String uuid = repoId +widthQ + heightQ +colorQ;
        Optional<RepositoryRender> repoRender = q.<RepositoryRender>All(RepositoryRender.find).stream().filter(x->x.metrictype.equals(uuid)).findAny();
        if(repoRender.isPresent()){
            return  ok(new java.io.File( Play.application().path().getAbsolutePath()+repoRender.get().localpath));
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
        Query q = new Query();
        List<RepositoryRender> repositoryVersionList = q.All(RepositoryRender.find);

        if(repositoryVersionList.size()<0){
            return ok(listRepository.render(new ArrayList<>()));
        }

        for(RepositoryRender v:repositoryVersionList){

                v.setNameToDisplay(v.repository.url);
                v.setNumberOfFileToDispaly((int)q.NumberOfFile(v.repositoryversion.id));

        }

        return ok(listRepository.render( repositoryVersionList.stream().filter(x -> x.metrictype.contains("Discussion and import percentage")).collect(Collectors.toList())));
    }


    public static Result renderRepo(String id, Long version) {

        Map<String, String> info = new HashMap<>();

        Query q = new Query();
            info.put("size",Long.toString(q.TotalSize(version)));
          info.put("nol",Long.toString(q.TotalNumberOfCodeLines(version)));
            info.put("name",(q.ProjectName(version)));
            info.put("nod",Long.toString(q.NumberOfFile(version)));



        Map<String, String> getMapMethod = new HashMap();
        getMapMethod.put("Width: Field Count","depthMetrics");
        getMapMethod.put("Height: Method  Count","heightMetrics");
        getMapMethod.put("Color","colorMetrics");



        return ok(render.render(id, (long) version, QueryBuilder.getFilters(version), getMapMethod, info));
//        return ok();
    }

    public static Result getAllRepositoryRender(Long repositoryVersion){
        Query q = new Query();
       RepositoryVersion repo =  q.<RepositoryVersion>byId(new Pair<>(repositoryVersion, RepositoryVersion.find)).orElseThrow(()-> new CustomException("T"));
        List<RepositoryRender> repositoryRenders = repo.repositoryRenderList;
        System.out.println(repositoryRenders.size());
        class ResultType{
            public String url;
            public String type;
        }
        return ok(Json.stringify(Json.toJson(repo.repositoryRenderList.stream()
                .map(x->{
            ResultType r = new ResultType();
            r.type = x.metrictype;
            r.url = x.localpath;
            return r;
        })
        .collect(Collectors.toList()))));
    }

    public static Result applyFilter() {
        Http.RequestBody body = request().body();
        String data = body.asText();
        Data d = QueryBuilder.QueryBuilder(data);
        JsonNode result =(QueryBuilder.query(d));
        return ok(Json.stringify(result));
    }

    public static Result getFilters(int id) {
        System.out.println(id);
        return ok(Json.stringify(QueryBuilder.getFilters((long)id)));
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

    static String computeKey(String mCall,int par){
        return mCall+"("+par+")";
    }


    public static Result getDiscussion()  {
//

//
        final String path = request().body().asJson().get("path").asText();
        final HashMap<String,List<String>> resul = new HashMap<>();
        Query q = new Query();

        List<JavaMethodCall> jmc = q.AllDiscussedMethod(path);
        jmc.forEach(x->{
            if(!x.methodname.contains("$")) {
                String key = computeKey(x.methodname,x.params);
                if (resul.containsKey(key)) {
                    resul.get(key).addAll(x.methodDiscussionList.stream().map(z -> z.discussion.url).limit(10).collect(Collectors.toList()));
                } else {
                    resul.put(key, new ArrayList<>());
                    resul.get(key).addAll(x.methodDiscussionList.stream().map(z -> z.discussion.url).limit(10).collect(Collectors.toList()));
                }
            }
        });


        List<JavaImport> ji = q.AllDiscussedImport(path);
        ji.forEach(x->{
            if(!x.packageName.contains("$")) {
                if (resul.containsKey(x.packageName)) {
                    resul.get(x.packageName).addAll(x.importDiscussionList.stream().map(z -> z.discussion.url).limit(10).collect(Collectors.toList()));
                } else {
                    resul.put(x.packageName, new ArrayList<>());
                    resul.get(x.packageName).addAll(x.importDiscussionList.stream().map(z -> z.discussion.url).limit(10).collect(Collectors.toList()));
                }
            }
        });


//        Query.AllDiscussedMethod(path)
//                .stream()
//                .flatMap(x ->x.methodDiscussionList.stream()).collect(Collectors.toList())
//                .forEach(x->{
//                    if(resul.containsKey(x.javaMethod.name)){
//                        resul.get(x.javaMethod.name).add(x.discussion.url);
//                    }else{
//                        resul.put(x.javaMethod.name,new ArrayList<>());
//                        resul.get(x.javaMethod.name).add(x.discussion.url);
//
//                    }
//                });
//        final HashMap<String,List<String>> resul = new HashMap<>();
//        Query.AllDiscussedMethod(path)
//                .stream()
//                .map(x -> new Pair<String,List<String>>(x.methodname, x.getListOfMethodDiscussion().stream().map(y -> y.getDiscussionConcrete().getDiscussionURL()).collect(Collectors.toList())))
//                .forEach(x->{
//                    if(resul.containsKey(x.getKey())){
//                        resul.get(x.getKey()).addAll(x.getValue());
//                    }else{
//                        resul.put(x.getKey(),x.getValue());
//                    }
//                });
//        Query.AllDiscussedImport(path)
//                .stream()
//                .map(x -> new Pair<String,List<String>>(x.getPackageDiscussion(), x.getListOfImportDiscussion().stream().map(y -> y.getDiscussionConcrete().getDiscussionURL()).collect(Collectors.toList())))
//                .forEach(x -> {
//                    if (resul.containsKey(x.getKey())) {
//                        resul.get(x.getKey()).addAll(x.getValue());
//                    } else {
//                        resul.put(x.getKey(), x.getValue());
//                    }
//                });
               return ok(Json.stringify(Json.toJson(resul)));

    }
}
