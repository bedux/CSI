package logics.analyzer.analysis;

import interfaces.Analyser;
import interfaces.Component;
import logics.models.db.ComponentInfo;
import logics.models.db.RepositoryVersion;
import java.util.List;

/**
 * This Analyser has the goal of store the data computed in the database given a RepositoryVersion
 * If you add any other metrics add also here
 */

public class SaveFeaturesInDbAnalyser implements Analyser<Boolean> {
    private RepositoryVersion repositoryVersion;
    public SaveFeaturesInDbAnalyser(RepositoryVersion repositoryVersion){
        this.repositoryVersion = repositoryVersion;
    }
    @Override
    public Boolean analysis(Component value) {
        value.getComponentList().stream().forEach((x) -> x.applyFunction((new SaveFeaturesInDbAnalyser(repositoryVersion))::analysis));
        List<ComponentInfo> componentInfoList = ComponentInfo.find.select("*").where().eq("fileName",value.getFeatures().getPath()).eq("repository.id",repositoryVersion.id).findList();
        if(componentInfoList.size()==1){
            componentInfoList.get(0).setNOM(value.getFeatures().getMethodsNumber());
            componentInfoList.get(0).setWC((int)value.getFeatures().getWordCount());
            componentInfoList.get(0).setSize((int)value.getFeatures().getSize());
            componentInfoList.get(0).update(componentInfoList.get(0).id);
        }
        return null;
    }
}
