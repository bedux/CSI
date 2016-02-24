package interfaces;

import logics.Status;
import logics.versionUtils.VersionBranch;
import logics.versionUtils.VersionCommit;

import java.util.List;
import java.util.Objects;


public interface VersionedSystem {

    public Status.State clone();

    public List<VersionCommit> getCommit();

    public List<VersionBranch> getBranch();

    public void checkoutRevison(VersionCommit commitInfo);


    }
