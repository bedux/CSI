package logics.versionUtils;


import java.util.Date;

/**
 * Created by bedux on 23/02/16.
 */
public class VersionCommit {

    private final String message;
    private final String title;
    private final String name;
    private final Object data;
    private final Date date;

    public VersionCommit(String message, String title, String name, Date date, Object data) {
        this.message = message;
        this.title = title;
        this.name = name;
        this.date = date;
        this.data = data;
    }

    public Date getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    public Object getData() {
        return data;
    }

    @Override
    public String toString() {
        return ("Title: " + this.title + " Message:" + this.message + " Name:" + this.name + " Data:" + this.date.toString());
    }
}
