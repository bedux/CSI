package logics.models.db;

import play.db.ebean.Model;

import javax.persistence.*;

/**
 * Created by bedux on 24/02/16.
 */
@Entity
@Table(name="REPO_VERSION")
public class RepoVersion extends Model {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Long id;

    public String objectId;  //should be the revision





}
