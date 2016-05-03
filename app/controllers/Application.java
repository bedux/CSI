package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import exception.CustomException;
import exception.SQLnoResult;
import logics.Definitions;
import logics.databaseCache.DatabaseModels;
import logics.databaseUtilities.Pair;
import logics.models.db.*;
import logics.models.modelQuery.*;
import logics.models.query.QueryList;
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
import play.mvc.Result;
import views.html.listRepository;
import views.html.render;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Application extends Controller {

    private static HashMap<String,Function<String,Long>> queryAvailable = new HashMap<String,Function<String,Long>>(){{
        put("FieldCount",Query::CountFieldByPath);
        put("MethodsCount",Query::CountMethodByPath);
        put("DiscussionCount",Query::DiscussedImportMethodCounter);
        put("JavaDoc",Query::JavaDocForMethodCount);

    }};

    public static Result index() {
        return getRepositories();
    }


    public static Result indexGet(int id) {
        Long ids = (long)id;

        DatabaseModels.getInstance().invalidCache();
        Optional<RepositoryVersion> repositoryVersion = Query.byId(new Pair<>(ids, RepositoryVersion.class));
        repositoryVersion.orElseThrow(()-> new SQLnoResult());

        new PipelineManager().StoreAndAnalyze(repositoryVersion.get());

//        for (JavaFile file : DatabaseModels.getInstance().getAll(JavaFile.class)) {
//            ModelsQueryCountMethodByPath md = new ModelsQueryCountMethodByPath();
//            System.out.println(md.executeAndGetResult(file.getPath()));
//            file.getListOfJavaClass().stream().forEach(x->System.out.println(x.getId()));
//            file.getListOfJavaEnum().stream().forEach(x->System.out.println(x.getId()));
//            file.getListOfJavaImport().stream().forEach(x->System.out.println(x.getId()));
//            file.getListOfJavaInterface().stream().forEach(x->System.out.println(x.getId()));
//            file.getListOfObject().stream().forEach(x->System.out.println(x.getId()));
//
//            file.getListOfJavaClass().stream().flatMap(x -> x.getListOfMethod().stream()).map(x->x.getJavaClassConcrete().getJavaFileConcrete()).forEach(x -> System.out.println(x.getId()));
//            file.getListOfJavaClass().stream().flatMap(x -> x.getListOfField().stream()).map(x -> x.getJavaClassConcrete().getJavaFileConcrete()).forEach(x -> System.out.println(x.getId()));
//
//            file.getListOfJavaInterface().stream().flatMap(x -> x.getListOfMethod().stream()).map(x -> x.getJavaClassConcrete().getJavaFileConcrete()).forEach(x -> System.out.println(x.getId()));
//            file.getListOfJavaInterface().stream().flatMap(x -> x.getListOfField().stream()).map(x->x.getJavaClassConcrete().getJavaFileConcrete()).forEach(x -> System.out.println(x.getId()));
//        }


//        JavaFile fProxy =  (JavaFile)  Enhancer.create(JavaFile.class, new MethodInterceptor() {
//            @Override
//            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
//                Setter sett;
//                if((sett = method.getAnnotation(Setter.class))!=null){
//                    System.out.println("Found annotation Setter "+method.getName());
//                    Field field;
//
//                    if((field=o.getClass().getSuperclass().getDeclaredField(sett.fieldName()))!=null){
//                        field.setAccessible(true);
//                        if(field.getAnnotation(OneToMany.class)!=null) {
//                            Class<?> t = (Class<?>) ((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0];
//                            Field reff = Stream.of(t.getFields()).filter(x -> x.getAnnotation(ManyToOne.class) != null).filter(x -> x.getType().equals(o.getClass().getSuperclass())).findFirst().get();
//
//                        }else{
//                            System.out.println("No one to many");
//                        }
//
//                    }else{
//                        throw new CustomException("Setter annotation bad formed! on "+method.getName());
//                    }
//                }
//                return null;
//            }
//        });


        //fProxy.getId();
//
//        Optional<JavaFile> jf = DatabaseModels.getInstance().getEntity(JavaFile.class,149164);
//
//
////        System.out.println(jf.get().getJson().noLine);
////        System.out.println(jf.get().getId());
//
//
//        Optional<JavaClass> jc = DatabaseModels.getInstance().getEntity(JavaClass.class,2102272);
//     //   System.out.println(jc.get().getId());
//



//        Optional<JavaFile> jf = DatabaseModels.getInstance().getEntity(JavaFile.class,149164);
//        System.out.println(jf.get().getRepositoryVersionConcrete().getId());
//        jf.get().getListOfJavaClass().stream().flatMap(x->x.getListOfMethod().stream()).forEach(x->System.out.print(x.getJson().signature));
//        jf.get().getListOfJavaClass().stream().flatMap(x->x.getListOfField().stream()).forEach(x -> System.out.print(x.getJson().name));
//
//        jf.get().getListOfJavaImport().stream().map(x->x.getJavaFileConcrete().getId()).forEach(System.out::println);
//
//        DatabaseModels.getInstance().getAll(JavaFile.class).forEach(x->System.out.println(x.getName() + " " + x.getId()));
//        DatabaseModels.getInstance().getEntity(JavaFile.class,149150).get().getListOfJavaClass().forEach(
//                x->{
//                   MethodInfoJSON mij =  x.getJson();
//                    mij.signature = "Mamma";
//                    x.setJson(mij);
//                    System.out.println(x.getId());
//
//                }
//
//
//        );

//        System.out.println(jf.get().getListOfJavaClass().size()+"Size of JavaCLass");
//        System.out.println(jf.get().getListOfJavaInterface().size()+"Size of JavaInterface");
//        System.out.println(jf.get().getListOfJavaEnum().size()+"Size of Enum");
//        System.out.println(jf.get().getListOfJavaImport().size()+"Size of Import");
//        System.out.println(jf.get().getListOfJavaPackage().size()+"Size of Package");
//
//        Optional<Repository> repo = DatabaseModels.getInstance().getEntity(Repository.class,3);
//        repo.get().getListOfRepositoryVersion().stream().flatMap(x -> x.getListOfRepositoryRender().stream()).map(x->x.getRepositoryVersion()).
//        forEach(x -> System.out.println(x.getRepository().getId()));
//
//
//        JavaFile js = DatabaseModels.getInstance().getEntity(JavaFile.class).get();
//
//
//
//        JavaInterface ji = DatabaseModels.getInstance().getEntity(JavaInterface.class).get();
//        js.addJavaInterface(ji);
//        System.out.println(ji.getJavaFileConcrete().getId());
//
//      //  System.out.println(js.getId());
//        System.out.println(ji.getId()+"IF JI");




//        System.out.println(repo.get().getListOfRepositoryVersion().size());
//        System.out.println(jf.get().getListOfObject().size()+"Size of All");



//        jf.get().getListOfJavaClass().stream ().forEach(x -> System.out.println(x.getJson().lineStart));
//        System.out.println(jf.get().getListOfJavaClass().size());

//        Optional<Repository> r = DatabaseModels.getInstance().getEntity(Repository.class,8);
//        System.out.println(r.get().url);
//        r = DatabaseModels.getInstance().getEntity(Repository.class,8);
//        r = DatabaseModels.getInstance().getEntity(Repository.class,8);

//        try {
//            DatabaseModels.getInstance().invalidCache();
//           System.out.println(QueryList.getInstance().getAllJavaMethodFormPath("5/app/logics/models/json/RenderComponent.java").size());
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }


//        Optional<JavaFile> jf = DatabaseModels.getInstance().getEntity(JavaFile.class,5002);
//        Optional<JavaInterface> ji = DatabaseModels.getInstance().getEntity(JavaInterface.class);
//        jf.get().getListOfJavaInterface();
//        jf.get().addListOfJavaInterface(ji.get());
//        System.out.println(jf.get().getListOfJavaInterface().size());

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


    public static Result customComputation(){
        System.out.println("asd");
        Long repoId  = request().body().asJson().get("repositoryId").asLong();
        String widthQ  = request().body().asJson().get("width").asText();
        String heightQ  =request().body().asJson().get("height").asText();
        String colorQ  =request().body().asJson().get("color").asText();



        System.out.println(repoId +" "+widthQ+" "+heightQ+" "+colorQ+" ");



        //TODO
        /*
            Generate string that gives an idea about the kind of metrics to compute, this name should be also use as return value.
            use a name such that become possible to apply chacing over it.

           like:
           CUSTOM_ WIDTH Name _ height Name _ color Name
         */
        RepositoryVersion repo = Query.byId(new Pair<>(repoId, RepositoryVersion.class)).get();
        TreeGeneratorHandlerResult treeForG = new TreeGenerator().process(new TreeGeneratorHandleParam(repo));
        String uuid = UUID.randomUUID().toString();

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
        System.out.println("HEEEEEEERE");
        JsonNode ca  = new AnalyserHandler().process(result6).json;
        Result res = ok(Json.stringify(ca));
        System.out.println("RESULT!");
        return res;
    }

    public static Result getRepositories() {
        List<RepositoryRender> repositoryVersionList = Query.All(RepositoryRender.class);
        for(RepositoryRender v:repositoryVersionList){

                v.setNameToDisplay(   Query.ProjectName(v.getRepositoryVersion().getId()));
                v.setNumberOfFileToDispaly(  Query.NumberOfFile(v.getRepositoryVersion().getId()));

        }

        return ok(listRepository.render(repositoryVersionList));
    }


    public static Result renderRepo(String id, Long version) {

        Map<String, String> info = new HashMap<>();

            info.put("size",Long.toString(Query.TotalSize(version)));
            info.put("nol",Long.toString(Query.TotalNumberOfCodeLines(version)));
            info.put("name",(Query.ProjectName(version)));
            info.put("nod",Long.toString(Query.NumberOfFile(version)));



        Map<String, String> getMapMethod = new HashMap();
        getMapMethod.put("Method Count Width/Depth","heightMetrics");
        getMapMethod.put("Fields Count Height","depthMetrics");
        getMapMethod.put("Color","colorMetrics");



        return ok(render.render(id, (long) version, null, getMapMethod, info));
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
//        return ok(Json.stringify(Json.toJson(ComponentInfo.find.where().eq("fileName", id).findList())));
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



//            JavaFile jf = new JavaFileByPath().execute(path).orElseThrow(() -> new SQLnoResult());
//
//
//            List<ImportTable> javaImports = new AllDiscussionImport().execute(new AllNonLocalImport().execute(new Pair<>(jf.getId(), jf.getRepositoryVersionConcrete().getId())));
//
//
//            final List<String> allJavaMethods  = new AllJavaMethodOfRepositoryVersion().execute(jf.getRepositoryVersionConcrete().getId());
//
//            final List<String> currentMethods =  new AllJavaMethodCallFormPath().execute(path).stream().collect(Collectors.toList());
//
//            final List<String> javaMethods =currentMethods.stream().distinct().filter(x -> !allJavaMethods.stream().anyMatch(y -> y.equals(x))).collect(Collectors.toList());
//
//
//        final HashMap<String,List<String>> resul = new HashMap<>();
//
//            javaMethods.stream().forEach(x -> {
//                       List<StackOFDiscussion> sovfd = new AllDiscussionHavingMethodName().execute(new ArrayList<String>() {{
//                           add(x);
//                       }});
//                       sovfd.forEach(z->{
//                           if(resul.containsKey(x)){
//                             resul.get(x).add(z.getDiscussionURL());
//                           }else{
//                               resul.put(x,new ArrayList<String>(){{add(z.getDiscussionURL());}});
//                           }
//                       });
//
//
//            });
//
//            javaImports.stream().forEach(x -> {
//
//                    List<StackOFDiscussion> sovfd = new SOFDiscussionFromImportDiscussion().execute(new ArrayList<ImportTable>() {{
//                        add(x);
//                    }});
//                    sovfd.forEach(z->{
//                        if(resul.containsKey(x)){
//                            resul.get(x.getPackageDiscussion()).add(z.getDiscussionURL());
//                        }else{
//                            resul.put(x.getPackageDiscussion(),new ArrayList<String>(){{add(z.getDiscussionURL());}});
//                        }
//                    });
//
//
//            });
//        System.out.println(Json.toJson(resul));
//            String res = Json.stringify(Json.toJson(resul));
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
//            javaImports.stream().map(x->x.methodName).forEach(Logger::info);
//
//            result.addAll(discussionsReleatedToMethodName);
//            String res1 = Json.stringify(Json.toJson(result.stream().map(x->x.methodName).distinct().collect(Collectors.toList())));
//            return ok(res1);




    }
}
