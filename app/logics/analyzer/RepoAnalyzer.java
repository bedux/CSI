package logics.analyzer;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import exception.CustumException;
import interfaces.Component;
import logics.models.db.Repo;
import logics.models.tools.MaximumMinimumData;
import logics.renderTools.BinaryTreePack;
import play.libs.Json;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

    public JsonNode getTree() {
        File f = new File("./repoDownload/" + repo.id);
        Package root = new Package(new Features("root", repo.id.toString(), f.toPath()));
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
        System.out.println(mmd);
        root.applyFunction(new AdjustSizeAnalyser(mmd)::analysis);

        root.applyFunction(new PackingAnalyzer()::analysis);

        int max = root.applyFunction(new DepthAnalyser()::analysis);
        root.applyFunction(new ColoringAnalyser(max)::analysis);



        return (Json.toJson(root.getRenderJSON()));



    }

    private String clearPath(String s) {
        return s.substring(s.indexOf("repoDownload/" + repo.id) + ("repoDownload/").length());

    }


}




