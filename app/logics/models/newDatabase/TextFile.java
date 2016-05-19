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
@Table(name="text_file")

public class TextFile  extends Model {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="repository_id_seq")
    public long id;

    public String name;

    @JsonIgnore
    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="repo_version",referencedColumnName="id")
    public RepositoryVersion repositoryVersion;

    public static Finder find = new Finder(Long.class, TextFile.class);

    public long size;


}
