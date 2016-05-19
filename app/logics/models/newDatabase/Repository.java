package logics.models.newDatabase;


import com.avaje.ebean.annotation.CacheStrategy;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by bedux on 18/05/16.
 */
@CacheStrategy

@Entity
@Table(name="repository")
public class Repository  extends Model {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="repository_id_seq")
    public long id;

    public String url;

    public String usr;

    public String pwd;

    public String localpath;

    public String subversiontype;

    public static Finder find = new Finder(Long.class, Repository.class);

    @OneToMany(mappedBy="repository",cascade = CascadeType.ALL)
    public  List<RepositoryRender> repositoryRenderList;

    @OneToMany(mappedBy="repository",cascade = CascadeType.ALL)
    public  List<RepositoryVersion> repositoryVersionList;
}
