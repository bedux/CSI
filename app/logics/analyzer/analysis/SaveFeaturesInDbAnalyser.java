package logics.analyzer.analysis;

import interfaces.Analyser;
import interfaces.Component;
import logics.analyzer.DataFeatures;
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
        List<ComponentInfo> componentInfoList = ComponentInfo.find.where().eq("fileName",value.getFeatures().getPath()).eq("repository.id",repositoryVersion.id).findList();

        if(componentInfoList.size()==1){

            ComponentInfo target = componentInfoList.get(0);
            DataFeatures source = value.getFeatures();

            target.setNoF(source.getNoF());
            target.setNoForeachSTM(source.getNoForeachSTM());
            target.setNoForSTM(source.getNoForSTM());
            target.setNoIf(source.getNoIf());
            target.setNoLine(source.getNoLine());
            target.setNoMethod(source.getNoMethod());
            target.setNoPrivateMethod(source.getNoPrivateMethod());
            target.setNoPublicMethod(source.getNoPublicMethod());
            target.setNoProtectedMethod(source.getNoProtectedMethod());
            target.setNoWhile(source.getNoWhile());
            target.setNoWord(source.getNoWord());


            componentInfoList.get(0).update(componentInfoList.get(0).id);
        }
        return null;
    }
}
