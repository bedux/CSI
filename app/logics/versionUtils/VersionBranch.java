package logics.versionUtils;

/**
 * Created by bedux on 23/02/16.
 */
public class VersionBranch {

    private final String name;

    public VersionBranch(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return ("Name: " + name);
    }
}
