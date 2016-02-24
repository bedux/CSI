package logics.analyzer;

import interfaces.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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
        System.out.print(this.features.getPath()+"\n");
        componentList.stream().forEach(Component::operation);

        return features;
    }

    @Override
    public Features operation(Features features) {
        return null;
    }

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

                    Pakage p = new Pakage(new Features(toSearch,toSearch));
                    p.add(toSearch,f,remain);
                    componentList.add(p);


                }else{
                    //add file
                    if(Files.isReadable(f)){
                        DataFile file = new DataFile(new Features(search,this.features.getPath()+"/"+remainPath));
                        componentList.add(file);
                    }else{
                        BinaryFile file = new BinaryFile(new Features(search,this.features.getPath()+"/"+remainPath));
                        componentList.add(file);
                    }
                }

                return true;
            }else{
                return false;
            }
    }
}
