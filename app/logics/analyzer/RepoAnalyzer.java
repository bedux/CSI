package logics.analyzer;

import com.fasterxml.jackson.databind.JsonNode;
import exception.CustumException;
import logics.analyzer.analysis.*;
import logics.models.db.Repository;
import logics.models.tools.MaximumMinimumData;
import play.libs.Json;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;

/**
 * Created by bedux on 24/02/16.
 */
public class RepoAnalyzer {

    private Repository repository;

    public RepoAnalyzer(Repository repository) {
        this.repository = repository;
    }

    public JsonNode getTree() {
        File f = new File("./repoDownload/" + repository.id);
        Package root = new Package(new Features("root", repository.id.toString(), f.toPath()));
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
            throw new CustumException(e1);
        }


        root.applyFunction(new WordCountAnalyser()::analysis);
        root.applyFunction(new MethodCountAnalyser()::analysis);
        MaximumMinimumData mmd = root.applyFunction(new MaximumDimentionAnalyser()::analysis);
        root.applyFunction(new AdjustSizeAnalyser(mmd)::analysis);
        root.applyFunction(new PackingAnalyzer()::analysis);
        int max = root.applyFunction(new DepthAnalyser()::analysis);
        root.applyFunction(new ColoringAnalyser(max)::analysis);


        return (Json.toJson(root.getRenderJSON()));



    }

    private String clearPath(String s) {
        return s.substring(s.indexOf("repoDownload/" + repository.id) + ("repoDownload/").length());

    }


}




