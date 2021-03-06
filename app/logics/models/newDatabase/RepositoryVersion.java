package logics.models.newDatabase;


import com.avaje.ebean.annotation.CacheStrategy;
import com.fasterxml.jackson.annotation.JsonIgnore;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by bedux on 18/05/16.
 */
@CacheStrategy(naturalKey = "localpath")

@Entity
@Table(name="repositoryversion")
public class RepositoryVersion  extends Model {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="repository_id_seq")
    public long id;

    public String localpath;

    @JsonIgnore
    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "repository", columnDefinition = "repository")
    public Repository repository;

    @JsonIgnore

    @OneToMany(mappedBy="repositoryVersion",cascade = CascadeType.ALL)
    public List<BinaryFile> binaryFileList;

    @JsonIgnore
    @OneToMany(mappedBy="repositoryVersion",cascade = CascadeType.ALL)
    public List<JavaFile> javaFileList;

    @JsonIgnore
    @OneToMany(mappedBy="repositoryVersion",cascade = CascadeType.ALL)
    public List<TextFile> textFileList;

    @JsonIgnore
    @OneToMany(mappedBy="repositoryversion",cascade = CascadeType.ALL)
    public List<RepositoryRender> repositoryRenderList;

    public static Finder find = new Finder(Long.class, RepositoryVersion.class);


}
