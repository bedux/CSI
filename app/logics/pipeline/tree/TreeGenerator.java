package logics.pipeline.tree;

import exception.CustomException;
import interfaces.Handler;
import logics.Definitions;
import logics.analyzer.*;
import logics.analyzer.Package;
import logics.models.db.File;
import logics.models.db.RepositoryVersion;
import logics.models.query.QueryList;


import java.util.List;


public class TreeGenerator implements Handler<TreeGeneratorHandleParam, TreeGeneratorHandlerResult> {

    /***
     *
     * @param  param TreeGeneratorHandleParam contains the repository version
     * @return a new tree
     */
    @Override
    public TreeGeneratorHandlerResult process(TreeGeneratorHandleParam param) {
        List<File> components;
        try {
            components = QueryList.getInstance().getFileByRepositoryVersion(param.repositoryVersion.id);
        } catch (Exception e) {
            throw new CustomException(e);
        }


        java.io.File fileRoot = new java.io.File(Definitions.repositoryPath + param.repositoryVersion.id);
        Package root = new Package(new Features("root", param.repositoryVersion.id.toString(), fileRoot.toPath()));
        for (logics.models.db.File component : components) {
            java.io.File helper = new java.io.File(Definitions.repositoryPath + component.path);
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