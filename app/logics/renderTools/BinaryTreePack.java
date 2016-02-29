package logics.renderTools;

import interfaces.Component;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Created by bedux on 29/02/16.
 */
public class BinaryTreePack {

    private Node root;


    public static  void funcToCall(Component c){
        if(c.getComponentList().size()==0)return;
        BinaryTreePack r;
        Stream<Component> strm = c.getComponentList().stream().sorted((y,x)->((x.getFeatures().getWidth()+x.getFeatures().getDeep())-(y.getFeatures().getWidth() + y.getFeatures().getDeep()))<0?-1:1);
        r = new BinaryTreePack(strm.findFirst().get());
        c.getComponentList().stream().sorted((y,x)->((x.getFeatures().getWidth()+x.getFeatures().getDeep())-(y.getFeatures().getWidth() + y.getFeatures().getDeep()))<0?-1:1).forEachOrdered((x -> r.insert(x)));
        float deep = (float) c.getComponentList().stream().max((x,y)->x.getFeatures().getBoundingBox().getRight()<y.getFeatures().getBoundingBox().getRight()?-1:1).get().getFeatures().getBoundingBox().getRight();
        float width =(float) c.getComponentList().stream().max((x,y)->x.getFeatures().getBoundingBox().getButton()<y.getFeatures().getBoundingBox().getButton()?-1:1).get().getFeatures().getBoundingBox().getButton();
        c.getFeatures().setDeep(deep);
        c.getFeatures().setWidth(width);

    }
    public BinaryTreePack(Component rootCP){
        root = new Node(rootCP.getFeatures().getBoundingBox());
    }


    public void insert(Component component){
        Node toInsert = root.insert(component);
        if(toInsert!=null){
            toInsert = toInsert.split(component);
            toInsert.assign(component);
            component.getFeatures().setBB(toInsert.getBoundingBox());
            System.out.println(toInsert.getBoundingBox().toString()+"asdasd1");
            System.out.println( component.getFeatures().getBoundingBox());

        }else{
             resize(component);
            Node n = root.insert(component);
            if(n!=null) {
                n = n.split(component);
                n.assign(component);
                component.getFeatures().setBB(n.getBoundingBox());
                System.out.println(n.getBoundingBox().toString()+"asdasd2");
                System.out.println( component.getFeatures().getBoundingBox());

            }else{
                System.out.println("   asdasd   ");
            }


        }


    }

    public Node resize(Component c){
        BoundingBox target = c.getFeatures().getBoundingBox();

        boolean cantGoButton =  target.getWidth()<=root.getBoundingBox().getWidth();
        boolean cantGoRight =  target.getHeight()<=root.getBoundingBox().getHeight();

        boolean shouldGoRight =  cantGoButton && (root.getBoundingBox().getWidth() + target.getWidth() <= root.getBoundingBox().getHeight());
        boolean shouldGoButton = cantGoRight && (root.getBoundingBox().getHeight() + target.getHeight() <= root.getBoundingBox().getWidth());


        if(shouldGoRight){
            return grawRight(target);
        }else if(shouldGoButton){
            return grawDown(target);

        }else if(cantGoRight){
            return  grawRight(target);
        }else if(cantGoButton){
            return grawDown(target);

        }else return null;



    }

    public Node grawDown(BoundingBox bb){
            Node helper = root;
            root = new Node(new BoundingBox(
                                            0,
                                            0,
                                            root.getBoundingBox().getWidth(),
                                            root.getBoundingBox().getButton()+bb.getHeight()));


            root.setbottom(new Node(new BoundingBox(
                    0,
                    helper.getBoundingBox().getHeight(),
                    helper.getBoundingBox().getWidth(),
                    bb.getHeight(),1
                    )));

            root.setRight(helper);
//        System.out.println(bb+ " current 0 ");
//        System.out.println(root.getBoundingBox()+ " current 0 ");
//        System.out.println(root.getBottom().getBoundingBox()+ " Button ");
//        System.out.println(root.getRight().getBoundingBox()+ " Right ");
        root.assign();
        return  root;
    }

    public Node grawRight(BoundingBox bb){
        Node helper = root;
        root = new Node(new BoundingBox(0,
                                        0,
                                        root.getBoundingBox().getRight()+bb.getWidth(),
                                        root.getBoundingBox().getHeight()));

        root.setRight(new Node(new BoundingBox(
                helper.getBoundingBox().getWidth(),
                                                0,
                                                bb.getWidth(),
                helper.getBoundingBox().getHeight(),1)));

        root.setbottom(helper);

//        System.out.println(bb+ " current 1 ");
//        System.out.println(root.getBoundingBox()+ " current 1 ");
//        System.out.println(root.getBottom().getBoundingBox()+ " Button ");
//        System.out.println(root.getRight().getBoundingBox()+ " Right ");
        root.assign();
        return  root;

    }
}
