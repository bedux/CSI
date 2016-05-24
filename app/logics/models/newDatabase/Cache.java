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
@Table(name="cache")
public class Cache extends Model {
    @Id
    @Column(columnDefinition = "TEXT")
    public String id;

    public Cache(String id){
        this.id = id;
    }

    public long countFieldByPath=-1;
    public long countMethodByPath=-1;
    public long discussedImportMethodCounter=-1;
    public long discussedImportMethodCounterOverTotal=-1;
    public long javaDocForMethodCount=-1;
    public long javaDocOverTotalMethods=-1;

    public static Finder find = new Finder(String.class, Cache.class);

}
