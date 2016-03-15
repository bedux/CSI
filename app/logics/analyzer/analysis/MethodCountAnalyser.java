package logics.analyzer.analysis;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import exception.CustomException;
import interfaces.Analyser;
import interfaces.Component;
import logics.analyzer.*;
import logics.analyzer.Package;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * Created by bedux on 03/03/16.
 */
public class MethodCountAnalyser implements Analyser<Integer> {


    @Override
    public Integer analysis(Component c) {
        c.getComponentList().stream().forEach((x) -> x.applyFunction((new MethodCountAnalyser())::analysis));
        int n = 0;
        if (c instanceof BinaryFile) {
            n=0;
        } else if (c instanceof DataFile) {
            n=analysisCast((DataFile)c);
        }else if (c instanceof logics.analyzer.Package){
            n=analysisCast((Package) c);
        }
        c.getFeatures().setMethodsNumber(n);
        return n;
    }

    private Integer analysisCast(Package c){
            return 0;
    }

    private Integer analysisCast(DataFile c){
        String fn = c.getFeatures().getPath().substring(c.getFeatures().getPath().lastIndexOf(".") + 1);

        if(fn.indexOf("java")==0) {
            try (InputStream is = Files.newInputStream(c.getFeatures().getFilePath())) {
                CompilationUnit p = JavaParser.parse(is);

                class Increment{
                    int i = 0;
                }
                class MethodVisitor extends VoidVisitorAdapter<Increment> {
                    @Override
                    public void visit(MethodDeclaration n, Increment arg) {
                        super.visit(n, arg);
                        arg.i++;
                   }
                }
                Increment i =new Increment();
                new MethodVisitor().visit(p, i);
                is.close();
                return i.i;

            } catch (IOException e) {
                throw new CustomException(e);
            } catch (ParseException e) {
                throw new CustomException(e);
            }
        }else{
            return 0;
        }

    }


}
