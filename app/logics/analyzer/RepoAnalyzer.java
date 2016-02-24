package logics.analyzer;

import logics.models.Repo;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;

/**
 * Created by bedux on 24/02/16.
 */
public class RepoAnalyzer{

    private Repo repo;
    public RepoAnalyzer(Repo repo){
        this.repo=repo;
    }

    public void getTree(){
        File f = new File("./repoDownload/"+repo.id);
        Pakage root=new Pakage(new Features("root",repo.id.toString()));
        try {
            Files.walk( FileSystems.getDefault().getPath(f.getAbsolutePath())).forEach((x)->{

                if(Files.isRegularFile(x)){
                        String s = clearPath(x.normalize().toString());

                        String dir = s.substring(0,s.indexOf('/'));
                         String remainName = s.substring(s.indexOf('/')+1);



                        String requiredName = f.getAbsolutePath().substring(f.getAbsolutePath().indexOf(dir));
                        root.add(requiredName,x.getFileName(),remainName);

                }
            });
        }catch (IOException e1) {
                e1.printStackTrace();
        }
    }

    private String clearPath(String s){
        return s.substring(s.indexOf("repoDownload/"+repo.id)+("repoDownload/").length());

    }


}
