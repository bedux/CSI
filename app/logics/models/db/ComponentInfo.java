package logics.models.db;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by bedux on 04/03/16.
 */
@Entity
@Table(name="REPO_INFO")
public class ComponentInfo extends Model {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long id;

    public String parent;

    public String fileName;

    public int NOM;

    public int size;

    public int WC;

    @ManyToOne
    public RepositoryVersion repository;


    public static ComponentInfo createComponentInfo(RepositoryVersion repo,String fileName){
        ComponentInfo componentInfo = new ComponentInfo();
        componentInfo.repository = repo;
        componentInfo.fileName = fileName;
        componentInfo.save();
        return componentInfo;
    }

    public static Finder<Long,ComponentInfo> find = new Finder<Long, ComponentInfo>(Long.class,ComponentInfo.class);


}