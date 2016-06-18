package logics.versionUtils;

import controllers.WebSocketConnection;
import exception.CustomException;
import interfaces.VersionedSystem;
import logics.Definitions;
import logics.models.newDatabase.Repository;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import play.Logger;
import play.Play;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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

    /***
     *
     * @param repository Constructor
     */
    public GitRepo(Repository repository) {
        this.repository = repository;

    }


    public  GitRepo(Repository repo,String s){
        try {
            git = Git.open( new File( Play.application().path().getAbsolutePath()+"/"+Definitions.repositoryPathABS+repo.id));
            System.out.println("CREATEEEEE");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /***
     *
     * @param name clone the repository and store with the current na,e
     * @return
     */

    @Override
    public Definitions.State clone(String name) {
        repoFile = new File( Play.application().path().getAbsolutePath()+"/"+Definitions.repositoryPathABS + name);

        if (Files.exists(repoFile.toPath())) {
            throw new CustomException("Id already exist. Please clear the directory of GIT projects");
        }

        final CloneCommand clone = Git.cloneRepository();
        clone.setURI(repository.url);
        Logger.info(repository.usr + " " + repository.pwd);

        //Login require?
        if (repository.usr != null && repository.pwd != null) {
            clone.setCredentialsProvider(new UsernamePasswordCredentialsProvider(repository.usr, repository.pwd));
        }


        clone.setDirectory(repoFile);
        clone.setProgressMonitor((WebSocketProgress)WebSocketConnection.sockHandler(repository.id));
        try {
            git = clone.call();
        } catch (GitAPIException e) {
            Logger.info(e.getMessage());
            throw new CustomException(e);
        }

        return Definitions.State.COMPLETE;

    }


    /**
     *
     * @return get all repository Branch
     */
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
    public void checkoutRevision(VersionCommit commitInfo) {

    }

    /**
     *
     * @return Get all repository Commits
     */
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

    /**
     *
     * @param obj
     */
    public void checkoutRevision(VersionCommit obj,String id) {
//        try {
//            RevCommit data = (RevCommit) obj.getData();
//            ResetCommand checkoutCommand = git.reset();
//
//            checkoutCommand.setRef("HEAD").setMode(ResetCommand.ResetType.HARD).call();
//            commits = null;
//        } catch (GitAPIException e) {
//            throw new CustomException(e);
//        }

        Runtime rt = Runtime.getRuntime();

        try {
            Process pr = rt.exec("git -C " + Play.application().path().getAbsolutePath() + "/" + Definitions.repositoryPathABS + id + " reset --hard "+obj.getName());
            pr.waitFor();

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(pr.getInputStream()));

            String line = "";
            while ((line = reader.readLine())!= null) {
                System.out.println(line + "\n");
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return Get the current version of the system
     */
    @Override
    public String getCurrentVersion() {
        return getCommit().get(0).getName();
    }
}
