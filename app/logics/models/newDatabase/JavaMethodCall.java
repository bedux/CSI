package logics.models.newDatabase;

import com.avaje.ebean.annotation.CacheStrategy;
import com.fasterxml.jackson.annotation.JsonIgnore;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by bedux on 18/05/16.
 */
@CacheStrategy(naturalKey = "methodname")

@Entity
@Table(name="method")
public class JavaMethodCall  extends Model {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="repository_id_seq")
    public long id;


    public String methodname;

    public int params;

    @JsonIgnore
    @OneToMany(mappedBy="javaMethod",cascade = CascadeType.ALL)
    public List<MethodDiscussion> methodDiscussionList;

    @JsonIgnore
    @OneToMany(mappedBy="javaMethodCall",cascade = CascadeType.ALL)
    public List<MethodFile> methodFileList;

    public static Finder find = new Finder(Long.class, JavaMethodCall.class);

}
