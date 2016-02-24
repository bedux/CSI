package logics.models;

import interfaces.VersionedSystem;
import logics.data.RepoForm;
import logics.versionUtils.GitRepo;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.io.IOException;

/**
 * Created by bedux on 22/02/16.
 */
@Entity
@Table(name="REPO")
public class Repo extends Model {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long id;

    public String user;

    @Constraints.Required
    public String uri;

    public String pwd;

    @ManyToOne
    public User usr;


    public String type;

    public static Finder<Long,Repo> find = new Finder<Long, Repo>(Long.class,Repo.class);



    /**
     * @param user
     * @param uri
     * @param pwd
     * @param usr
     */
    public static Repo CreareRepo(String user,String uri ,String pwd,User usr){
        Repo r = new Repo();
        r.user = user;
        r.pwd = pwd;
        r.uri = uri;
        r.usr = usr;
        r.save();
        return r;
    }
    public static Repo CreareRepo(RepoForm repoForm){
        Repo r = new Repo();
        r.user = repoForm.user;
        r.pwd = repoForm.pwd;
        r.uri = repoForm.uri;
        r.type = repoForm.type;
        r.save();
        return r;
    }

    public  VersionedSystem CreateSystem() throws IOException {
            if(this.type.equalsIgnoreCase("GIT")){
                    return  new GitRepo(this);
            }
        else throw new IOException();
    }
}
