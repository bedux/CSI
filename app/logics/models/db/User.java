package logics.models.db;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="USER")
public class User extends Model {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Constraints.Min(10)
    public Long id;


    @Constraints.Required
    @Column(unique=true)
    public String name;


    @OneToMany(mappedBy = "usr", cascade = CascadeType.ALL)
    public List<Repository> repositories;


    public static Finder<Long,User> find = new Finder<Long, User>(Long.class,User.class);

}
