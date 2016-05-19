package logics.models.newDatabase;

import com.avaje.ebean.annotation.CacheStrategy;
import com.fasterxml.jackson.annotation.JsonIgnore;
import play.db.ebean.Model;

import javax.persistence.*;

/**
 * Created by bedux on 18/05/16.
 */
@CacheStrategy(naturalKey = "name")

@Entity
@Table(name="java_package")
public class JavaPackage  extends Model {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="repository_id_seq")
    public long id;


    @JsonIgnore
    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="java_file",referencedColumnName="id")
    public JavaFile javaFile;

    public String name;

    public static Finder find = new Finder(Long.class, JavaPackage.class);


}
