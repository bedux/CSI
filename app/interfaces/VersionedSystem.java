package interfaces;

import logics.Definitions;
import logics.versionUtils.VersionBranch;
import logics.versionUtils.VersionCommit;

import java.util.List;


public interface VersionedSystem {

    public Definitions.State clone(String name);

    public List<VersionCommit> getCommit();

    public List<VersionBranch> getBranch();

    public void checkoutRevison(VersionCommit commitInfo);

    String getCurrentVersion();

}
