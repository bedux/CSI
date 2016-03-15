package logics.models.db;

import exception.CustomException;
import interfaces.VersionedSystem;
import logics.models.form.RepoForm;
import logics.versionUtils.GitRepo;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;

/**
 * Created by bedux on 22/02/16.
 */
@Entity
@Table(name="Repository")
public class Repository extends Model {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long id;

    public String user;

    @Constraints.Required
    public String uri;

    public String pwd;

    @ManyToOne
    public User usr;
//
//    @OneToMany(cascade = CascadeType.ALL)
//    public List<RepositoryVersion> repositoryVersions;

    public String type;

    public static Finder<Long,Repository> find = new Finder<Long, Repository>(Long.class,Repository.class);



    /**
     * @param user
     * @param uri
     * @param pwd
     * @param usr
     */
    public static Repository CreareRepo(String user,String uri ,String pwd,User usr){
        Repository r = new Repository();
        r.user = user;
        r.pwd = pwd;
        r.uri = uri;
        r.usr = usr;
        r.save();
        return r;
    }

    public static Repository CreareRepo(String user,String uri ,String pwd,String type){
        Repository r = new Repository();
        r.user = user;
        r.pwd = pwd;
        r.uri = uri;
        r.type = type;
        r.save();
        return r;
    }
    public static Repository CreareRepo(RepoForm repoForm){
        Repository r = new Repository();
        r.user = repoForm.user;
        r.pwd = repoForm.pwd;
        r.uri = repoForm.uri;
        r.type = repoForm.type;
        r.save();
        return r;
    }

    public  VersionedSystem CreateSystem()  {

            if(this.type.equalsIgnoreCase("GIT")){
                    return  new GitRepo(this);
            }else{
                throw new CustomException();
            }

    }
}
