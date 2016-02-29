package controllers;

import logics.analyzer.Features;
import logics.renderTools.PakageNode;
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

//        PakageNode root = new PakageNode();
//        Features f = new Features("","",null);
//        f.setSize(100000);
//        f.setWorldCount(100000);
//
//        Features f1 = new Features("","",null);
//        f1.setSize(10);
//        f1.setWorldCount(10);
//
//
//
//       root.insert(f);
//
//      root.insert(f1);
//
//
//        Features f2 = new Features("","",null);
//        f2.setSize(5);
//        f2.setWorldCount(5);
//        root.insert(f2);
//
//       root.insert(f2);
   //     return ok(index.render("cazzo"));

        return ok(render.render());
    }






}
