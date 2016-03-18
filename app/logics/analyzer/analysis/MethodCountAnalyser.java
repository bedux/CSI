package logics.analyzer.analysis;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.ForeachStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import exception.CustomException;
import interfaces.Analyser;
import interfaces.Component;
import interfaces.DataAttributes;
import logics.analyzer.BinaryFile;
import logics.analyzer.DataFeatures;
import logics.analyzer.DataFile;
import logics.analyzer.Package;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.nio.file.Files;

/**
 * Created by bedux on 03/03/16.
 */
public class MethodCountAnalyser implements Analyser<DataAttributes> {


    @Override
    public DataAttributes analysis(Component c) {
        c.getComponentList().stream().forEach((x) -> x.applyFunction((new MethodCountAnalyser())::analysis));
        if (c instanceof BinaryFile) {

        } else if (c instanceof DataFile) {
            analysisCast((DataFile)c);
        }else if (c instanceof logics.analyzer.Package){
            analysisCast((Package) c);
        }
        c.getFeatures().bindingToPakageble();
        return c.getFeatures();
    }

    private DataAttributes analysisCast(Package c){
            return c.getFeatures();
    }

    private DataAttributes analysisCast(DataFile c){
        String fn = c.getFeatures().getPath().substring(c.getFeatures().getPath().lastIndexOf(".") + 1);

        if(fn.indexOf("java")==0) {
            try (InputStream is = Files.newInputStream(c.getFeatures().getFilePath())) {
                CompilationUnit p = JavaParser.parse(is);
                c.getFeatures().setNoLine(p.getEndLine()-p.getBeginLine());

                class MethodVisitor extends VoidVisitorAdapter<DataFeatures> {
                    @Override
                    public void visit(MethodDeclaration n, DataFeatures arg) {
                        super.visit(n, arg);
                        arg.setNoMethod(arg.getNoMethod()+1);
                        if(Modifier.isPrivate(n.getModifiers())){
                            arg.setNoPrivateMethod(arg.getNoPrivateMethod()+1);
                        }else if(Modifier.isPublic(n.getModifiers())){
                            arg.setNoPublicMethod(arg.getNoPublicMethod() + 1);
                        }else if(Modifier.isProtected(n.getModifiers())){
                            arg.setNoProtectedMethod(arg.getNoProtectedMethod() + 1);
                        }
                   }

                    @Override
                    public void visit(FieldDeclaration n,DataFeatures o){
                        super.visit(n,o);
                        o.setNoF(o.getNoF() + n.getVariables().size());

                    }

                    @Override
                    public void visit(ForeachStmt n,DataFeatures o){
                        super.visit(n,o);
                        o.setNoForeachSTM(o.getNoForeachSTM()+1);


                    }

                    @Override
                    public void visit(ForStmt n,DataFeatures o){
                        super.visit(n,o);
                        o.setNoForSTM(o.getNoForSTM()+1);


                    }
                    @Override
                    public void visit(WhileStmt n,DataFeatures o){
                        super.visit(n,o);
                        o.setNoWhile(o.getNoWhile() + 1);

                    }

                    @Override
                    public void visit(IfStmt n,DataFeatures o){
                        super.visit(n,o);
                        o.setNoIf(o.getNoIf()+1);

                    }


                }
                new MethodVisitor().visit(p, c.getFeatures());
                is.close();
                return c.getFeatures();

            } catch (IOException e) {
                throw new CustomException(e);
            } catch (ParseException e) {
                throw new CustomException(e);
            }
        }else{
            return c.getFeatures();
        }

    }


}
