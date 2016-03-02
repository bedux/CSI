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
import logics.renderTools.BinaryTreePack;
import play.libs.Json;

import java.io.File;
import java.io.FileInputStream;
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


        root.applyIndependent(this::wordCount);
//        root.applyIndependent(this::printStatistics);
        root.applyIndependent(BinaryTreePack::funcToCall);
//
//
       return (Json.toJson(root.applyRenderer()));



    }

    private String clearPath(String s) {
        return s.substring(s.indexOf("repoDownload/" + repo.id) + ("repoDownload/").length());

    }

    private void wordCount(Component c) {
        if (c instanceof BinaryFile) {
//           System.out.println("BinaryFile");


        } else if (c instanceof DataFile) {

            try (Stream<String> fileLinesStream = Files.lines(c.getFeatures().getFilePath())) {
                c.getFeatures().setWordCount(fileLinesStream.filter((line) -> {
                    return line.replace("\\s+", "").length() > 0;
                }).mapToInt((line) -> {
                    line = line.trim();
                    return line.split("\\s+").length;
                }).sum());
                c.getFeatures().setSize(Files.size(c.getFeatures().getFilePath()));

                c.getFeatures().setSegment((int) (Math.random() * 20 + 3));

            } catch (IOException e) {
                throw new CustumException(e);
            }


            ///TEST OF THE LIBRARY

            String fn = c.getFeatures().getPath().substring(c.getFeatures().getPath().lastIndexOf(".") + 1);

            if(fn.contains(".java")) {
                try (InputStream is = Files.newInputStream(c.getFeatures().getFilePath())) {
                    CompilationUnit p = JavaParser.parse(is);

                    class Increment{
                        int i = 0;
                    }

                    class MethodVisitor extends VoidVisitorAdapter<Increment> {

                        @Override
                        public void visit(MethodDeclaration n, Increment arg) {
                            arg.i++;
                            super.visit(n, arg);
                        }
                    }


                    Increment i =new Increment();
                    new MethodVisitor().visit(p, i);
                    c.getFeatures().setMethodsNumber(i.i);

                    is.close();

                } catch (IOException e) {
                    throw new CustumException(e);
                } catch (ParseException e) {
                    throw new CustumException(e);
                }
            }else{
                c.getFeatures().setMethodsNumber(5);
            }

        } else if (c instanceof Package) {

            float w = 0;
            for(Component c1 :c.getComponentList())
                w+=c1.getFeatures().getHeight();
            c.getFeatures().setHeight(10);
            float f = (float)Math.random();
            c.getFeatures().setColor(new float[]{f,f,f});

        }else{

        }
    }
}




