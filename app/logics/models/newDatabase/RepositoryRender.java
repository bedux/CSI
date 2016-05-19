package logics.models.newDatabase;


import com.avaje.ebean.annotation.CacheStrategy;
import com.fasterxml.jackson.annotation.JsonIgnore;
import play.db.ebean.Model;

import javax.persistence.*;

/**
 * Created by bedux on 18/05/16.
 */
@CacheStrategy(naturalKey = "localpath")

@Entity
@Table(name="repository_render")
public class RepositoryRender extends Model {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="repository_id_seq")
    public long id;


    @JsonIgnore
    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "repository", columnDefinition = "id")
    public Repository repository;

    @JsonIgnore
    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "repositoryversion", columnDefinition = "id")
    public RepositoryVersion repositoryversion;


    public String localpath;

    public String metrictype;

    public static Finder find = new Finder(Long.class, RepositoryRender.class);

    @Transient
    public String getNameToDisplay() {
        return nameToDisplay;
    }
    @Transient
    public void setNameToDisplay(String nameToDisplay) {
        this.nameToDisplay = nameToDisplay;
    }
    @Transient
    public int getNumberOfFileToDispaly() {
        return numberOfFileToDispaly;
    }
    @Transient
    public void setNumberOfFileToDispaly(int numberOfFileToDispaly) {
        this.numberOfFileToDispaly = numberOfFileToDispaly;
    }

    @Transient
    private String nameToDisplay;
    @Transient
    private int  numberOfFileToDispaly;

}
