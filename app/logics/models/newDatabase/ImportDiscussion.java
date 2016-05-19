package logics.models.newDatabase;


import com.avaje.ebean.annotation.CacheStrategy;
import play.db.ebean.Model;

import javax.persistence.*;

/**
 * Created by bedux on 18/05/16.
 */
@CacheStrategy

@Entity
@Table(name="import_discussion")
public class ImportDiscussion  extends Model {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="repository_id_seq")
    public long id;


    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="idd",referencedColumnName = "id")
    public Discussion discussion;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="idi",referencedColumnName = "id")
    public JavaImport javaImport;

    public static Finder find = new Finder(Long.class, ImportDiscussion.class);




}
