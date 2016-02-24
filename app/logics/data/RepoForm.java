package logics.data;

import play.data.validation.Constraints;

/**
 * Created by bedux on 23/02/16.
 */
public class RepoForm {

    public String user;

    @Constraints.Required
    public String uri;

    public String pwd;

    public String type;

}
