package logics.models.newDatabase;


import com.avaje.ebean.annotation.CacheStrategy;
import com.fasterxml.jackson.annotation.JsonIgnore;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by bedux on 18/05/16.
 */
@CacheStrategy(naturalKey = "url")

@Entity
@Table(name="discussion")
public class Discussion  extends Model {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="repository_id_seq")
    public long id;

    public String url;

    @JsonIgnore
    @OneToMany(mappedBy="discussion",cascade = CascadeType.ALL)
    List<ImportDiscussion> importDiscussionList;

    @JsonIgnore
    @OneToMany(mappedBy="discussion",cascade = CascadeType.ALL)
    List<MethodDiscussion> methodDiscussionList;

    public static Finder find = new Finder(Long.class, Discussion.class);

}
