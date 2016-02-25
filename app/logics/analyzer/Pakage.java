package logics.analyzer;

import interfaces.Component;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Created by bedux on 24/02/16.
 */
public class Pakage implements Component {

    private Features features;
    private List<Component> componentList = new ArrayList<>();

    public Pakage(Features current){
        features = current;
    }

    @Override
    public Features operation() {
        System.out.print("Directory:" + this.features.getName()+"\n");
        componentList.stream().forEach(Component::operation);
        return features;
    }

    @Override
    public Features operation(Features features) {
        return null;
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

                    Pakage p = new Pakage(new Features(toSearch.substring(toSearch.lastIndexOf('/')+1),toSearch,f));
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
        componentList.parallelStream().forEach((x) -> x.applyIndependent(function));
        function.accept(this);
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



