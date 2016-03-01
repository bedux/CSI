package logics.analyzer;

import interfaces.Component;
import logics.models.json.RenderChild;
import logics.models.json.RenderComponent;
import logics.renderTools.Point3d;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;


public class Package implements Component {

    private Features features;
    private List<Component> componentList = new ArrayList<>();

    public List<Component> getComponentList(){
        return componentList;
    }
    public Package(Features current){
        features = current;
    }




    @Override
    public boolean add(String search,Path f,String remainPath) {
            if(features.getPath().equals(search)){
                if(remainPath.indexOf('/')!=-1) {
                    String toSearch = features.getPath() +"/"+remainPath.substring(0,remainPath.indexOf('/'));
//                    System.out.println(toSearch);
                    String remain = remainPath.substring(remainPath.indexOf('/')+1);
//                    System.out.println(remain);
                    for(Component c:componentList){
                       if( c.add(toSearch,f,remain)){
                           return true;
                       }
                    }

                    Package p = new Package(new Features(toSearch.substring(toSearch.lastIndexOf('/')+1),toSearch,f));
                    p.add(toSearch,f,remain);
                    componentList.add(p);


                }else{
                    String name = remainPath;

                    //add file
                    if(isTextFile(f)){
                        DataFile file = new DataFile(new Features(name,this.features.getPath()+"/"+remainPath,f));
                        componentList.add(file);
                    }else{
                        BinaryFile file = new BinaryFile(new Features(name,this.features.getPath()+"/"+remainPath,f));
                        componentList.add(file);
                    }
                }

                return true;
            }else{
                return false;
            }
    }

    @Override
    public void applyIndependent(Consumer<Component> function){
        componentList.stream().forEach((x) -> x.applyIndependent(function));
        function.accept(this);
    }


    @Override
    public RenderChild applyRenderer() {
        RenderChild[] renderComponent = componentList.stream().map((x)->x.applyRenderer()).toArray(x->new RenderChild[x]);
        return new RenderChild(new float[]{features.getBoundingBox().getLeft(),0,features.getBoundingBox().getTop()},new RenderComponent(this.getFeatures().getRendererWidth(),this.getFeatures().getHeight(),this.getFeatures().getRendererDeep(),this.getFeatures().getColor(),4,this.getFeatures().getPath(),renderComponent));
    }


    @Override
    public Features getFeatures() {
        return this.features;
    }
    private boolean isTextFile(Path p){
            try (Stream<String> fileLinesStream = Files.lines(p)){
                fileLinesStream.count();
                return true;

            } catch (Exception e) {
                return false;
            }

    }

}



