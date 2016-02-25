package controllers;

import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public static Result index() {

//
//        User usr = new User();
//        usr.name = "Marco ";
//        try {
//            usr.save();
//
//        }catch (Exception e){
//
//        }
//
//
//        User.find.all().stream().forEach((x)->System.out.print(x.id));





        return ok(render.render());
    }






}
