package logics.models.newDatabase;

import com.avaje.ebean.annotation.CacheStrategy;
import com.fasterxml.jackson.annotation.JsonIgnore;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by bedux on 18/05/16.
 */
@CacheStrategy(naturalKey = "package")

@Entity
@Table(name="import")
public class JavaImport  extends Model {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="repository_id_seq")
    public long id;


    @Column(name="package")
    public String packageName;


    @JsonIgnore
    @OneToMany(mappedBy="javaImport")
    public  List<ImportDiscussion> importDiscussionList;
    @JsonIgnore
    @OneToMany(mappedBy="javaImport",cascade = CascadeType.ALL)
    public List<ImportFile> importFileList;

    public static Finder find = new Finder(Long.class, JavaImport.class);

}
