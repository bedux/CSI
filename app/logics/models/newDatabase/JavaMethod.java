package logics.models.newDatabase;

import com.avaje.ebean.annotation.CacheStrategy;
import play.db.ebean.Model;

import javax.persistence.*;

/**
 * Created by bedux on 18/05/16.
 */
@CacheStrategy(naturalKey = "name")

@Entity
@Table(name="java_method")
public class JavaMethod   extends Model {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="repository_id_seq")
    public long id;


    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="java_file",referencedColumnName="id")
    public JavaFile javaFile;

    public String name;

    public static Finder find = new Finder(Long.class, JavaMethod.class);

}
