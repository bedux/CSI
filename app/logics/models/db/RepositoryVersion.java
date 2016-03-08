package logics.models.db;

import logics.models.form.RepoForm;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bedux on 07/03/16.
 */
@Entity
@Table(name="RepositoryVersion")
public class RepositoryVersion extends Model {


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Long id;

    public String hss;

    public int numOfFile;
    public String mama;


    public  void setnumOfFile(int n){
        this.numOfFile = n;
    }
    public void setJson(String json) {
        this.json = json;
    }

    public void setHss(String hss) {
        this.hss = hss;
    }

    public void setComponentInfo(List<ComponentInfo> componentInfo) {

        //this.componentInfo = componentInfo;
    }
    public void addComponentInfo(ComponentInfo componentInfo) {

       // this.componentInfo.add(componentInfo);
    }

    public String json;

//    @OneToMany(cascade = CascadeType.ALL)
//    public List<ComponentInfo> componentInfo;

    @ManyToOne
    public  Repository repository;

    public static RepositoryVersion RepositoryVersion(Repository repo){
        RepositoryVersion repoVersion = new RepositoryVersion();
        repoVersion.repository = repo;
        repoVersion.save();
      //  repoVersion.componentInfo = new ArrayList<>();
        return repoVersion;
    }
    public static Finder<Long,RepositoryVersion> find = new Finder<Long, RepositoryVersion>(Long.class,RepositoryVersion.class);

}
