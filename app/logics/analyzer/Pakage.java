package logics.analyzer;

import interfaces.Component;
import logics.models.json.RenderChild;
import logics.models.json.RenderComponent;
import logics.renderTools.BoundingBox;
import logics.renderTools.PakageNode;
import logics.renderTools.Point3d;
import play.Logger;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
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
        componentList.stream().forEach((x) -> x.applyIndependent(function));
        function.accept(this);
    }

    @Override
    public void applyIndependentArray(Consumer<List<Component>> function){
        componentList.stream().forEach((x) -> x.applyIndependentArray(function));
        function.accept(this.componentList);
    }




    @Override
    public RenderChild applyRenderer(Consumer<Component> function) {

        RenderChild[] renderComponent = componentList.stream().sorted((x,y)->{
            float w1 = x.getFeatures().getWidth()+x.getFeatures().getDeep();
            float w2 = y.getFeatures().getWidth()+y.getFeatures().getDeep();
            w1 = x.getFeatures().getHeight();
            w2 = y.getFeatures().getHeight();

            return w1<w2?-1:1;
        }).map((x) -> {
            return x.applyRenderer(function);
        }).toArray((x)->new RenderChild[x]);

        //packing all element siche i have data that contains a renderer component.]
        //set te position of the paking

        float w=0;
        float h=0;
        float d=0;
        for(RenderChild c:renderComponent){
            w += c.getFeatures().getWidth();
            h += c.getFeatures().getHeight();
            d += c.getFeatures().getDeep();


        }
        features.setHeight(h/2);


        PakageNode roots = new PakageNode();
        Features f1 = new Features("","",null);
        f1.setBB(new BoundingBox(Float.MAX_VALUE,Float.MAX_VALUE));
        roots.insert(f1);

        for(RenderChild r: renderComponent){
            PakageNode node = roots.insert(r);
            if(node==null){
                System.out.println();
                System.out.print(r.getFeatures().getBoundingBox().toString());
                System.out.println();

            }else {
                BoundingBox bb  = node.getBoundingBox();
                Point3d p = bb.getCenter();
                r.getFeatures().setBB(bb);
                r.position = new float[]{(float)p.getX(),this.features.getHeight() ,(float) p.getZ()};
            }
        }

        //now i should get the minimum w /
        float deep = (float)Arrays.stream(renderComponent).max((x,y)->x.getFeatures().getBoundingBox().getRight()<y.getFeatures().getBoundingBox().getRight()?-1:1).get().getFeatures().getBoundingBox().getRight();
        float width =(float) Arrays.stream(renderComponent).max((x,y)->x.getFeatures().getBoundingBox().getButton()<y.getFeatures().getBoundingBox().getButton()?-1:1).get().getFeatures().getBoundingBox().getButton();


        features.setWidth(width);
        features.setDeep(deep);

        RenderChild rc = new RenderChild(new float[]{0,features.getHeight(),0},new RenderComponent(width,features.getHeight(),deep,new float[]{(float)Math.random(),(float)Math.random(),(float)Math.random()},4,this.getFeatures().getPath(),renderComponent));
        rc.setFeatures(features);

        float max = width>deep?width:deep;
        rc.getFeatures().setBB(new BoundingBox(max,max));
        System.out.println(rc.toString());
        return rc;
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



