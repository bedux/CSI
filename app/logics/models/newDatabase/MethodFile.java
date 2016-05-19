package logics.models.newDatabase;


import com.avaje.ebean.annotation.CacheStrategy;
import com.fasterxml.jackson.annotation.JsonIgnore;
import play.db.ebean.Model;

import javax.persistence.*;

/**
 * Created by bedux on 18/05/16.
 */
@CacheStrategy

@Entity
@Table(name="method_file")
public class MethodFile  extends Model {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="repository_id_seq")
    public long id;


    @JsonIgnore
    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="idf",referencedColumnName = "id")
    public JavaFile javaFile;

    @JsonIgnore
    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="idm",referencedColumnName = "id")
    public JavaMethodCall javaMethodCall;



    public static Finder find = new Finder(Long.class, MethodFile.class);


}
