package logics.renderTools;

import interfaces.Component;

import java.util.Arrays;

/**
 * Created by bedux on 29/02/16.
 */
public class BinaryTreePack {

    private Node root;


    public static  void funcToCall(Component c){


        if(c.getComponentList().size()==0)return;

        BinaryTreePack r;
        Component[] strm = c.getComponentList().stream().sorted((x,y)-> {

            float a = x.getFeatures().getWidth() * x.getFeatures().getDeep();
            float b = y.getFeatures().getWidth() * y.getFeatures().getDeep();

            if(a==b)return 0;
            else return a>b?1:-1;
        }).toArray(x->new Component[x]);
        r = new BinaryTreePack(strm[0]);
        Arrays.stream(strm).forEach((x -> {
            if (x.getFeatures().getWidth() > 0 && x.getFeatures().getDeep() > 0)
                r.insert(x);
        }));


        float w=0;
        float d=0;
       for(Component x:c.getComponentList()){
             w = w < x.getFeatures().getBoundingBox().getRight()?x.getFeatures().getBoundingBox().getRight():w;
             d = d < x.getFeatures().getBoundingBox().getButton()?x.getFeatures().getBoundingBox().getButton():d;
       }
        c.getFeatures().getBoundingBox().setBB(new BoundingBox(d+c.getFeatures().gap,w+c.getFeatures().gap));
//        System.out.println( r.root.getBoundingBox());

    }
    public BinaryTreePack(Component rootCP){
        root = new Node(rootCP.getFeatures().getBoundingBox());
    }


    public void insert(Component component){
        Node toInsert = root.insert(component);
        if(toInsert!=null){
            toInsert = toInsert.split(component);
            toInsert.assign(component);
//
//            component.getFeatures().setWidth(toInsert.getBoundingBox().getWidth());
//            component.getFeatures().setDeep(toInsert.getBoundingBox().getDeep());


        }else{
             resize(component);
            Node n = root.insert(component);
            if(n!=null) {
                n = n.split(component);
                n.assign(component);

//                component.getFeatures().setWidth(n.getBoundingBox().getWidth());
//                component.getFeatures().setDeep(n.getBoundingBox().getDeep());

            }else{
            }


        }


    }

    public Node resize(Component c){
        BoundingBox target = c.getFeatures().getBoundingBox();

        boolean cantGoButton =  target.getWidth()<=root.getBoundingBox().getWidth();
        boolean cantGoRight =  target.getDeep()<=root.getBoundingBox().getDeep();

        boolean shouldGoRight =  cantGoRight && ((root.getBoundingBox().getWidth() + target.getWidth()) < root.getBoundingBox().getDeep());
        boolean shouldGoButton = cantGoButton && ((root.getBoundingBox().getDeep() + target.getDeep()) < root.getBoundingBox().getWidth());


        if(shouldGoRight){
            return growRight(target);
        }else if(shouldGoButton){
            return growBottom(target);

        }else if(cantGoRight){
            return  growRight(target);
        }else if(cantGoButton){
            return growBottom(target);

        }else return null;



    }

    public Node growBottom(BoundingBox bb){
            Node helper = root;
            root = new Node(new BoundingBox(
                                            0,
                                            0,
                                            root.getBoundingBox().getWidth(),
                                            root.getBoundingBox().getButton()+bb.getDeep()));


            root.setbottom(new Node(new BoundingBox(
                    0,
                    helper.getBoundingBox().getDeep(),
                    helper.getBoundingBox().getWidth(),
                    bb.getDeep(),1
                    )));

            root.setRight(helper);

        root.assign();
        return  root;
    }

    public Node growRight(BoundingBox bb){
        Node helper = root;
        root = new Node(new BoundingBox(0,
                                        0,
                                        root.getBoundingBox().getRight()+bb.getWidth(),
                                        root.getBoundingBox().getDeep()));

        root.setRight(new Node(new BoundingBox(
                helper.getBoundingBox().getWidth(),
                                                0,
                                                bb.getWidth(),
                helper.getBoundingBox().getDeep(), 1)));

        root.setbottom(helper);

//        System.out.println(bb+ " current 1 ");
//        System.out.println(root.getBoundingBox()+ " current 1 ");
//        System.out.println(root.getBottom().getBoundingBox()+ " Button ");
//        System.out.println(root.getRight().getBoundingBox()+ " Right ");
        root.assign();
        return  root;

    }
}
