package logics.versionUtils;

import logics.Status;
import logics.models.db.Repo;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bedux on 22/02/16.
 */
public class GitLoader {

    public static Status.State Load(Repo repo){
       final CloneCommand clone = Git.cloneRepository();
        clone.setURI(repo.uri);
        //Login require?
        if(repo.user!=null && repo.pwd !=null){
            clone.setCredentialsProvider(new UsernamePasswordCredentialsProvider(repo.user,repo.pwd));
        }

        clone.setDirectory(new File("./repoDownload/"+repo.id));

        clone.setProgressMonitor(new SimpleProgressMonitor());

        try {
            clone.call();
        } catch (GitAPIException e) {
            e.printStackTrace();
            return Status.State.ERROR;
        }

        return Status.State.COMPLETE;

    }

    public static List<Ref> GetAllVersions(Repo repo){
        try {
            final Git git = Git.open(new File("./repoDownload/" + repo.id));
            List<Ref> refs =  git.branchList().call();
            git.close();
            return  refs;

        }catch (Exception e){
            return null;
        }
    }

    public static ArrayList<RevCommit> GetAllCommit(Repo repo){
        try {
            final Git git = Git.open(new File("./repoDownload/" + repo.id));
            ArrayList<RevCommit> result = new ArrayList();
            git.log().all().call().forEach(result::add);
            git.close();

            return result;

        }catch (Exception e){
            return null;
        }
    }

    public static ArrayList<RevCommit> GetAllCommit(Repo repo,AnyObjectId objectId){
        try {
           final Git git = Git.open(new File("./repoDownload/" + repo.id));
            ArrayList<RevCommit> result = new ArrayList();
            RevCommit rv;
            git.log().add(objectId).call().forEach(result::add);
            git.close();
            return result;

        }catch (Exception e){
            return null;
        }
    }


    public static void getFromRevision(Repo repo,RevCommit revCommit){
        try {
        final Git git = Git.open(new File("./repoDownload/" + repo.id));
            Ref r = git.checkout().setStartPoint(revCommit).setAllPaths(true).call();

        }catch (Exception e){
            System.out.println(e.getMessage());

            return;
        }


    }
}
