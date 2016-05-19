package logics.models.newDatabase;

import com.avaje.ebean.annotation.CacheStrategy;
import com.fasterxml.jackson.annotation.JsonIgnore;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by bedux on 18/05/16.
 */
@CacheStrategy(naturalKey = "name")

@Entity
@Table(name="java_file")
public class JavaFile  extends Model {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="repository_id_seq")
    public long id;


    public String name;

    @JsonIgnore
    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="repo_version",referencedColumnName="id")
    public RepositoryVersion repositoryVersion;

    @JsonIgnore
    @OneToMany(mappedBy="javaFile",cascade = CascadeType.ALL)
    public List<JavaDoc> javaDocList;
    @JsonIgnore
    @OneToMany(mappedBy="javaFile",cascade = CascadeType.ALL)
    public List<JavaInterface> javaInterfaceList;
    @JsonIgnore
    @OneToMany(mappedBy="javaFile",cascade = CascadeType.ALL)
    public List<JavaMethod> javaMethodList;
    @JsonIgnore
    @OneToMany(mappedBy="javaFile",cascade = CascadeType.ALL)
    public List<JavaPackage> javaPackageList;
    @JsonIgnore
    @OneToMany(mappedBy="javaFile",cascade = CascadeType.ALL)
    public List<JavaClass> javaClassList;
    @JsonIgnore
    @OneToMany(mappedBy="javaFile",cascade = CascadeType.ALL)
    public List<JavaField> javaFieldList;

    @JsonIgnore
    @OneToMany(mappedBy="javaFile",cascade = CascadeType.ALL)
    public List<ImportFile> importFileList;
    @JsonIgnore
    @OneToMany(mappedBy="javaFile",cascade = CascadeType.ALL)
    public List<MethodFile> methodFileList;

    public static Finder find = new Finder(Long.class, JavaFile.class);

    public long size;

    public long nline;


}
