package controllers;

import exception.CustomException;
import logics.analyzer.RepoAnalyzer;
import logics.models.db.Repository;
import logics.models.form.RepoForm;
import logics.pipeline.PipelineManager;
import play.data.Form;
import play.libs.F;
import play.mvc.*;
import views.html.*;

/**
 * Created by bedux on 23/02/16.
 */
public class Registration extends Controller {

    public static Result repoRegistrationTemplate(){
        Form<RepoForm> repoForm = Form.form(RepoForm.class);
        return ok(repoRegistration.render(repoForm));
    }

    //Used for the repository registration submit. Save the repo onto the database
    public static F.Promise<Result> repoRegistrationSubmit(){

        Form<RepoForm> repoForm = Form.form(RepoForm.class).bindFromRequest();



        if(repoForm.hasErrors()){
            return F.Promise.promise(()->redirect("/addRepo"));
        }else{

            F.Promise<Result> trs =   F.Promise.promise(() ->
            {
                //Add to database the repository

                new PipelineManager().runPipeline(repoForm.get());
                return ok(index.render("Repository Added"));
            });

            return trs;

        }
    }

    public static F.Promise<Result> getFiles(){


            F.Promise<Result> trs =   F.Promise.promise(() ->
            {
                //Add to database the repository
                try {
                    RepoAnalyzer repoAnalyzer = new RepoAnalyzer(Repository.find.all().get(0));
                    repoAnalyzer.getTree();

                } catch (CustomException e) {
                    System.out.println(e.getException().getStackTrace().toString());
                }


                return ok(index.render("Repository Added"));
            });

            return trs;


    }


    public static Result addUser(){
        return ok("todo");
    }


}
