package logics.pipeline.analayser;

import interfaces.Handler;
import logics.analyzer.*;
import logics.analyzer.Package;
import logics.models.db.ComponentInfo;
import logics.models.db.RepositoryVersion;
import logics.models.tools.MaximumMinimumData;
import play.libs.Json;

import java.io.File;
import java.util.List;

/**
 * Created by bedux on 08/03/16.
 */
public class AnaliserHandler implements Handler<AnalyserHandlerParam,AnalyserHandlerResult>{

    @Override
    public AnalyserHandlerResult process(AnalyserHandlerParam param) {
       List<ComponentInfo> components =  ComponentInfo.find.where("repository = " + param.repositoryVersion.id).findList();

        File fileRoot = new File("./repoDownload/" + param.repositoryVersion.id);
        Package root = new Package(new Features("root", param.repositoryVersion.id.toString(), fileRoot.toPath()));
        for(ComponentInfo component: components){
            File helper = new File("./repoDownload/" + component.fileName);
            String s = clearPath(helper.toPath().normalize().toString(),param.repositoryVersion);
            String dir = s.substring(0, s.indexOf('/'));
            String remainName = s.substring(s.indexOf('/') + 1);
//            String requiredName = helper.getAbsolutePath().substring(helper.getAbsolutePath().indexOf(dir));
            root.add(dir, helper.toPath(), remainName);
        }

        root.applyFunction(new WordCountAnalyser()::analysis);
        root.applyFunction(new MethodCountAnalyser()::analysis);
        MaximumMinimumData mmd = root.applyFunction(new MaximumDimentionAnalyser()::analysis);
        root.applyFunction(new AdjustSizeAnalyser(mmd)::analysis);

        root.applyFunction(new PackingAnalyzer()::analysis);

        int max = root.applyFunction(new DepthAnalyser()::analysis);
        root.applyFunction(new ColoringAnalyser(max)::analysis);

        return new AnalyserHandlerResult(Json.toJson(root.getRenderJSON()));
    }

    private String clearPath(String s,RepositoryVersion rpv) {
        return s.substring(s.indexOf("repoDownload/" + rpv.id) + ("repoDownload/").length());

    }
}
