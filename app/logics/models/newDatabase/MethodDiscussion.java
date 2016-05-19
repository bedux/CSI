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
@Table(name="method_discussion")
public class MethodDiscussion  extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public long id;

    @JsonIgnore
    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="idd",referencedColumnName = "id")
    public Discussion discussion;

    @JsonIgnore
    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="idm",referencedColumnName = "id")
    public JavaMethod javaMethod;

    public static Finder find = new Finder(Long.class, MethodDiscussion.class);




}
