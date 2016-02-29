package controllers;

import logics.analyzer.RepoAnalyzer;
import logics.models.form.RepoForm;
import logics.versionUtils.RepositoryManager;
import logics.models.db.Repo;
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
                try {
                    Repo r = Repo.CreareRepo(repoForm.get());
                    RepositoryManager.getInstance().AddRepo(r);

                } catch (Exception e) {
                    return redirect("/addRepo");
                }
                return ok(index.render("Repo Added"));
            });

            return trs;

        }
    }

    public static F.Promise<Result> getFiles(){


            F.Promise<Result> trs =   F.Promise.promise(() ->
            {
                //Add to database the repository
                RepoAnalyzer repoAnalyzer = new RepoAnalyzer( Repo.find.all().get(0));

                repoAnalyzer.getTree();

                return ok(index.render("Repo Added"));
            });

            return trs;


    }


    public static Result addUser(){
        return ok("todo");
    }

}
