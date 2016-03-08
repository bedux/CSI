package logics.versionUtils;

import exception.CustumException;
import interfaces.VersionedSystem;
import logics.Status;
import logics.models.db.Repository;
import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bedux on 23/02/16.
 */
public class GitRepo implements VersionedSystem {

    private  File repoFile;
    private  Git git;
    private Repository repository;

    private List<VersionBranch> branches = null;
    private ArrayList<VersionCommit> commits = null;
    public GitRepo(Repository repository){
        this.repository = repository;

    }





    @Override
    public Status.State clone(String name) {
        repoFile = new File("./repoDownload/"+ name);

        final CloneCommand clone = Git.cloneRepository();
        clone.setURI(repository.uri);
        System.out.println(repository.user + " "+repository.pwd);

        //Login require?
        if(repository.user!=null && repository.pwd !=null){
            clone.setCredentialsProvider(new UsernamePasswordCredentialsProvider(repository.user, repository.pwd));
        }


        clone.setDirectory(repoFile);
        clone.setProgressMonitor(new SimpleProgressMonitor());
        try {
            git =clone.call();
        } catch (GitAPIException e) {
            System.out.print(e.getMessage());
            throw new CustumException(e);
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
            throw new CustumException(e);
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
            throw new CustumException(e);
        }
    }

    public void checkoutRevison(VersionCommit obj)  {
        try {

            RevCommit data = (RevCommit)obj.getData();
            CheckoutCommand checkoutCommand =git.checkout();
            checkoutCommand.setAllPaths(true).setForce(true).setName(data.getId().getName()).call();
            commits = null;
        } catch (GitAPIException e) {
            throw new CustumException(e);
        }

    }

    @Override
    public String getCurrentVersion() {
            return getCommit().get(0).getName();
    }
}
