package controllers;


import logics.models.form.RepoForm;
import logics.pipeline.PipelineManager;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.loading;
import views.html.repoRegistration;

/**
 * Created by bedux on 23/02/16.
 */
public class Registration extends Controller {

    public static Result repoRegistrationTemplate() {
        Form<RepoForm> repoForm = Form.form(RepoForm.class);
        return ok(repoRegistration.render(repoForm));
    }

    //Used for the repository registration submit. Save the repo onto the database
    public static Result repoRegistrationSubmit() {

        Form<RepoForm> repoForm = Form.form(RepoForm.class).bindFromRequest();

        Long current = new PipelineManager().runPipeline(repoForm.get());

        return ok(loading.render(current));
    }


    public static Result addUser() {
        return ok("todo");
    }


}
