package logics.renderTools;

import interfaces.Component;

/**
 * Created by bedux on 29/02/16.
 */
public class Node {
    private Component component=null;

    private Node left=null;

    private Node bottom = null;

    private boolean occupaid = false;

    public Node getRight(){
        return left;
    }

    public Node getBottom(){
        return bottom;
    }

    public BoundingBox getBoundingBox(){
        return this.boundingBox;
    }

    public void setRight(Node right){
        left=right;
    }
    public void setbottom(Node bottom){
        this.bottom=bottom;
    }


    private  BoundingBox boundingBox;

    public  Node(BoundingBox bb){
        this.boundingBox = bb;
    }

    public Node insert(Component component){
        if(!isEmpty()) {
            Node retur = null;
            if(left!= null) {
                retur = left.insert(component);
            }
            if(bottom!=null && retur==null) {
                retur = bottom.insert(component);
            }
            return retur;
        }else if((this.getBoundingBox().getWidth()>=component.getFeatures().getBoundingBox().getWidth())&& (this.getBoundingBox().getHeight()>=component.getFeatures().getBoundingBox().getHeight())){
            return this;
        }else{
            return null;
        }
    }

    public Node split(Component component){
            //orizontal splitt
//        boundingBox =       (new BoundingBox(boundingBox.getLeft(),
//                                             boundingBox.getTop(),
//                                             boundingBox.getLeft()+component.getFeatures().getBoundingBox().getWidth(),
//                                             boundingBox.getTop()+component.getFeatures().getBoundingBox().getHeight()));


        left  = new Node(new BoundingBox(boundingBox.getLeft()+component.getFeatures().getBoundingBox().getWidth(),
                                         boundingBox.getTop(),
                                         boundingBox.getWidth()-component.getFeatures().getBoundingBox().getWidth(),
                                         component.getFeatures().getBoundingBox().getHeight(),1));

        bottom = new Node(new BoundingBox(boundingBox.getLeft(),
                                          boundingBox.getTop()+component.getFeatures().getBoundingBox().getHeight(),
                                          boundingBox.getWidth(),
                                          boundingBox.getHeight()-component.getFeatures().getBoundingBox().getHeight(),1));


        return this;
    }

    public void assign(Component component){
        this.component = component;
        occupaid = true;
    }

    public void assign(){
        occupaid=true;
    }

    private boolean isEmpty(){
        return !occupaid;
    }




}
