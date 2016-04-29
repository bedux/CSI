package controllers;

import exception.CustomException;
import exception.SQLnoResult;
import logics.Definitions;
import logics.databaseCache.DatabaseModels;
import logics.models.db.*;
import logics.models.modelQuery.AllMethodDiscussed;
import logics.models.modelQuery.CountJavaDocMethodsByPath;
import logics.models.query.QueryList;
import play.Play;
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
        System.out.println(new CountJavaDocMethodsByPath().execute("3/org.eclipse.jgit/src/org/eclipse/jgit/api/errors/AbortedByHookException.java"));

//        DatabaseModels.getInstance().invalidCache();
//        RepositoryVersion repositoryVersion = null;
//        repositoryVersion = QueryList.getInstance().getRepositoryVersionById(id).orElseThrow(()-> new SQLnoResult());
//
//        new PipelineManager().StoreAndAnalyze(repositoryVersion);

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

    public static Result getRepositories() {
        List<RepositoryRender> repositoryVersionList = QueryList.getInstance().getRepositoryVersionRender();
        for(RepositoryRender v:repositoryVersionList){
            try {
                v.setNameToDisplay( QueryList.getInstance().getProjectName(v.getRepositoryVersion().getId()));
                v.setNumberOfFileToDispaly( QueryList.getInstance().getNumberOfFile(v.getRepositoryVersion().getId()));
            } catch (SQLException e) {

                e.printStackTrace();
            }
        }

        return ok(listRepository.render(repositoryVersionList));
    }


    public static Result renderRepo(String id, Long version) {

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
        try {
            String path = request().body().asJson().get("path").asText();
            JavaFile jf = QueryList.getInstance().getJavaFileByPath(path).orElseThrow(() -> new SQLnoResult());
            List<ImportTable> javaImports = QueryList.getInstance().getAllDiscussionImport(QueryList.getInstance().getAllNonLocalImport(jf.getId(), jf.getRepositoryVersionConcrete().getId()));

            final List<String> allJavaMethods  = QueryList.getInstance().getAllJavaMethodOfRepositoryVersion(jf.getRepositoryVersionConcrete().getId());
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
                             resul.get(x).add(z.discussionURL);
                           }else{
                               resul.put(x,new ArrayList<String>(){{add(z.discussionURL);}});
                           }
                       });

                   } catch (SQLException e) {
                       e.printStackTrace();
                   }
            });

            javaImports.stream().forEach(x -> {
                try {
                    List<StackOFDiscussion> sovfd = QueryList.getInstance().getGitDiscussionFromImportDiscussion(new ArrayList<ImportTable>() {{
                        add(x);
                    }});
                    sovfd.forEach(z->{
                        if(resul.containsKey(x)){
                            resul.get(x.packageDiscussion).add(z.discussionURL);
                        }else{
                            resul.put(x.packageDiscussion,new ArrayList<String>(){{add(z.discussionURL);}});
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
//            javaImports.stream().map(x->x.methodName).forEach(Logger::info);
//
//            result.addAll(discussionsReleatedToMethodName);
//            String res1 = Json.stringify(Json.toJson(result.stream().map(x->x.methodName).distinct().collect(Collectors.toList())));
//            return ok(res1);
        }catch (SQLException e){
            throw new CustomException(e);
        }
        catch (Exception e){
            return ok("Error");
        }


    }
}
