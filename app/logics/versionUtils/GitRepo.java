package logics.versionUtils;

import interfaces.VersionedSystem;
import logics.Status;
import logics.models.db.Repo;
import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bedux on 23/02/16.
 */
public class GitRepo implements VersionedSystem {

    private final File repoFile;
    private  Git git;
    private Repo repo;

    private List<VersionBranch> branches = null;
    private ArrayList<VersionCommit> commits = null;
    public GitRepo(Repo repo){
        this.repo = repo;
        repoFile = new File("./repoDownload/"+repo.id);

    }



    @Override
    public Status.State clone() {

        final CloneCommand clone = Git.cloneRepository();
        clone.setURI(repo.uri);

        //Login require?
        if(repo.user!=null && repo.pwd !=null){
            clone.setCredentialsProvider(new UsernamePasswordCredentialsProvider(repo.user,repo.pwd));
        }

        clone.setDirectory(repoFile);
        clone.setProgressMonitor(new SimpleProgressMonitor());
        try {
            git =clone.call();
        } catch (GitAPIException e) {
            e.printStackTrace();
            return Status.State.ERROR;
        }

        return Status.State.COMPLETE;

    }

    @Override
    public List<VersionBranch> getBranch() {
        if(branches!=null) return branches;
        try {
            branches = new ArrayList<>();
            git.branchList().call().forEach((x)->branches.add(new VersionBranch(x.getName())));
            return  branches;

        }catch (Exception e){
            return  new ArrayList<>();
        }
    }

    @Override
    public List<VersionCommit> getCommit() {
        if(commits!=null) return commits;

        try {
            commits = new ArrayList();
            git.log().all().call().forEach((x)->{
                commits.add(new VersionCommit(x.getFullMessage(),x.getShortMessage(),x.getName(),x.getAuthorIdent().getWhen(),x));
            });
            return commits;

        }catch (Exception e){
            return  new ArrayList<>();
        }
    }

    public void checkoutRevison(VersionCommit obj)  {
        try {

            RevCommit data = (RevCommit)obj.getData();
            CheckoutCommand checkoutCommand =git.checkout();
            checkoutCommand.setAllPaths(true).setForce(true).setName(data.getId().getName()).call();
            commits = null;
        } catch (GitAPIException e) {
            e.printStackTrace();
        }

    }
}
