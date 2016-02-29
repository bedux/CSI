package logics.analyzer;

import interfaces.Component;
import logics.models.db.Repo;
import logics.renderTools.BinaryTreePack;
import logics.renderTools.PakageNode;
import play.Logger;
import play.libs.Json;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.stream.Stream;

/**
 * Created by bedux on 24/02/16.
 */
public class RepoAnalyzer {

    private Repo repo;

    public RepoAnalyzer(Repo repo) {
        this.repo = repo;
    }

    public void getTree() {
        File f = new File("./repoDownload/" + repo.id);
        Pakage root = new Pakage(new Features("root", repo.id.toString(), f.toPath()));
        try {
            Files.walk(FileSystems.getDefault().getPath(f.getAbsolutePath())).forEach((x) -> {

                if (Files.isRegularFile(x)) {
                    String s = clearPath(x.normalize().toString());
                    String dir = s.substring(0, s.indexOf('/'));
                    String remainName = s.substring(s.indexOf('/') + 1);
                    String requiredName = f.getAbsolutePath().substring(f.getAbsolutePath().indexOf(dir));
                    root.add(requiredName, x, remainName);

                }
            });
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        PakageNode roots = new PakageNode();
        Features f1 = new Features("","",null);
        f1.setSize(Long.MAX_VALUE);
        f1.setWorldCount(Long.MAX_VALUE);
        roots.insert(f1);

        root.applyIndependent(this::worldCout);
//        root.applyIndependent(this::printStatistics);
        root.applyIndependent(BinaryTreePack::funcToCall);


        System.out.println(Json.stringify(Json.toJson(root.applyRenderer())));



//        root.applyIndependent(roots::insert);

       // System.out.println(Json.stringify(Json.toJson(root.applyRenderer(roots::insert))));
//        PakageNode pn = new  PakageNode();
//        pn.setBoundingBox();
//        root.applyIndependent();

    }

    private String clearPath(String s) {
        return s.substring(s.indexOf("repoDownload/" + repo.id) + ("repoDownload/").length());

    }

    private void worldCout(Component c) {
        if (c instanceof BinaryFile) {
//           System.out.println("BinaryFile");


        } else if (c instanceof DataFile) {

            try (Stream<String> fileLinesStream = Files.lines(c.getFeatures().getFilePath())) {
                c.getFeatures().setWorldCount(fileLinesStream.filter((line) ->{ return line.replace("\\s+","").length()>0;}).mapToInt((line) -> {
                    line = line.trim();
                    return line.split("\\s+").length;
                }).sum());
                c.getFeatures().setSize(Files.size(c.getFeatures().getFilePath()));

                c.getFeatures().setSegment((int) (Math.random() * 20 + 3));

            } catch (IOException e) {

            }


        } else if (c instanceof Pakage) {
            try {
                c.getFeatures().setSize(Files.size(c.getFeatures().getFilePath()));
                c.getFeatures().setWorldCount(1);
                c.getFeatures().setHeight(10);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{

        }
    }

    private void printStatistics(Component c){
        Logger.debug(c.getFeatures().toString());
    }
}




