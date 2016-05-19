package logics.pipeline.tree;

import interfaces.Handler;
import logics.Definitions;
import logics.analyzer.Features;
import logics.analyzer.Package;
import logics.models.newDatabase.BinaryFile;
import logics.models.newDatabase.JavaFile;
import logics.models.newDatabase.RepositoryVersion;
import logics.models.newDatabase.TextFile;
import play.Play;


public class TreeGenerator implements Handler<TreeGeneratorHandleParam, TreeGeneratorHandlerResult> {

    /***
     *
     * @param  param TreeGeneratorHandleParam contains the repository version
     * @return a new tree
     */
    @Override
    public TreeGeneratorHandlerResult process(TreeGeneratorHandleParam param) {




        System.out.println(Play.application().path().getAbsolutePath()+"/" + Definitions.repositoryPathABS + param.repositoryVersion.id);

        java.io.File fileRoot = new java.io.File(  Play.application().path().getAbsolutePath()+"/"+Definitions.repositoryPathABS + param.repositoryVersion.id);
        Package root = new Package(new Features("root", Long.toString(param.repositoryVersion.id), fileRoot.toPath()));

        for (JavaFile component : param.repositoryVersion.javaFileList) {
            java.io.File helper = new java.io.File(  Play.application().path().getAbsolutePath()+"/"+Definitions.repositoryPathABS + component.name);
            String s = clearPath(helper.toPath().normalize().toString(), param.repositoryVersion);
            String dir = s.substring(0, s.indexOf('/'));
            String remainName = s.substring(s.indexOf('/') + 1);
            root.add(dir, helper.toPath(), remainName);
        }

        for (BinaryFile component : param.repositoryVersion.binaryFileList) {
            java.io.File helper = new java.io.File(  Play.application().path().getAbsolutePath()+"/"+Definitions.repositoryPathABS + component.name);
            String s = clearPath(helper.toPath().normalize().toString(), param.repositoryVersion);
            String dir = s.substring(0, s.indexOf('/'));
            String remainName = s.substring(s.indexOf('/') + 1);
            root.add(dir, helper.toPath(), remainName);
        }

        for (TextFile component : param.repositoryVersion.textFileList) {
            java.io.File helper = new java.io.File(  Play.application().path().getAbsolutePath()+"/"+Definitions.repositoryPathABS + component.name);
            String s = clearPath(helper.toPath().normalize().toString(), param.repositoryVersion);
            String dir = s.substring(0, s.indexOf('/'));
            String remainName = s.substring(s.indexOf('/') + 1);
            root.add(dir, helper.toPath(), remainName);
        }
        return new TreeGeneratorHandlerResult(root,param.repositoryVersion);
    }


    /***
     *
     * @param s The Path
     * @param rpv Repository Version
     * @return remove the absolute path of s
     */
    private String clearPath(String s, RepositoryVersion rpv) {
        return s.substring(s.indexOf(Definitions.repositoryPathABS + rpv.id) + (Definitions.repositoryPathABS).length());

    }
}
