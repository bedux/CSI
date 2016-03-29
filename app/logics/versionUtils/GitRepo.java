package logics.versionUtils;

import exception.CustomException;
import interfaces.VersionedSystem;
import logics.Definitions;
import logics.models.db.Repository;
import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bedux on 23/02/16.
 */
public class GitRepo implements VersionedSystem {

    private File repoFile;
    private Git git;
    private Repository repository;

    private List<VersionBranch> branches = null;
    private ArrayList<VersionCommit> commits = null;

    public GitRepo(Repository repository) {
        this.repository = repository;

    }


    @Override
    public Definitions.State clone(String name) {
        repoFile = new File(Definitions.repositoryPath + name);
        if (Files.exists(repoFile.toPath())) {
            throw new CustomException("Id already exist. Please clear the directory of GIT projects");
        }

        final CloneCommand clone = Git.cloneRepository();
        clone.setURI(repository.url);
        System.out.println(repository.usr + " " + repository.pwd);

        //Login require?
        if (repository.usr != null && repository.pwd != null) {
            clone.setCredentialsProvider(new UsernamePasswordCredentialsProvider(repository.usr, repository.pwd));
        }


        clone.setDirectory(repoFile);
        clone.setProgressMonitor(new SimpleProgressMonitor());
        try {
            git = clone.call();
        } catch (GitAPIException e) {
            System.out.print(e.getMessage());
            throw new CustomException(e);
        }

        return Definitions.State.COMPLETE;

    }


    @Override
    public List<VersionBranch> getBranch() {
        if (branches != null) return branches;
        try {
            branches = new ArrayList<>();
            git.branchList().call().forEach((x) -> branches.add(new VersionBranch(x.getName())));
            return branches;

        } catch (Exception e) {
            throw new CustomException(e);
        }
    }

    @Override
    public List<VersionCommit> getCommit() {
        if (commits != null) return commits;

        try {
            commits = new ArrayList();
            git.log().all().call().forEach((x) -> {
                commits.add(new VersionCommit(x.getFullMessage(), x.getShortMessage(), x.getName(), x.getAuthorIdent().getWhen(), x));
            });
            return commits;

        } catch (Exception e) {
            throw new CustomException(e);
        }
    }

    public void checkoutRevison(VersionCommit obj) {
        try {

            RevCommit data = (RevCommit) obj.getData();
            CheckoutCommand checkoutCommand = git.checkout();
            checkoutCommand.setAllPaths(true).setForce(true).setName(data.getId().getName()).call();
            commits = null;
        } catch (GitAPIException e) {
            throw new CustomException(e);
        }

    }

    @Override
    public String getCurrentVersion() {
        return getCommit().get(0).getName();
    }
}
