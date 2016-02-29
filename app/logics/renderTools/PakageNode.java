package logics.renderTools;

import interfaces.Component;
import logics.analyzer.Features;
import logics.models.json.RenderChild;
import logics.models.json.RenderComponent;
import play.Logger;

/**
 * Created by bedux on 26/02/16.
 */
public class PakageNode {

    private PakageNode left =null;
    private PakageNode right =null;
    private Features features = null;
    private BoundingBox bb = null;
    private int insR = 0;
    private int insL = 0;
    private int added = 0;
    public void setFeature(Features feature){
        this.features = feature;
    }

    public void setBoundingBox(BoundingBox boundingBox){
        this.bb = boundingBox;

    }
    public PakageNode insert(Component f){
        return insert(f.getFeatures());
    }

    public PakageNode insert(RenderChild f){
        return insert(f.getFeatures());
    }


    public PakageNode insert(Features f){
        BoundingBox featureBb = f.getBoundingBox();


        if(!isLeaf()){
                if(insR>insL) {
                    insL+=f.getWidth()+f.getHeight();
                    System.out.println("left");
                    PakageNode newNode = left.insert(f);
                    if (newNode != null) return newNode;
                    return right.insert(f);
                }else{
                    insR+=f.getWidth()+f.getHeight();
                    System.out.println("right");
                    PakageNode newNode = right.insert(f);
                    if (newNode != null) return newNode;
                    return left.insert(f);
                }

        }


        if(this.features!=null){
            System.out.println("leaf full");
            return null;
        }

        if(bb!=null && bb.isFit(featureBb) == BoundingBox.Fitting.SMALL){
            System.out.println("Small");
            return  null;
        }

        if(bb==null  ||  bb.isFit(featureBb)== BoundingBox.Fitting.PERFECT){
            if(bb==null){
                bb = featureBb;
            }else{
                this.features = f;
            }
            return  this;
        }

        this.left = new PakageNode();
        this.right = new PakageNode();

        double dw = bb.getWidth() - featureBb.getWidth();
        double dh = bb.getHeight() - featureBb.getHeight();
        System.out.println(featureBb.toString());
        if(dw > dh){
            left.setBoundingBox(new BoundingBox(bb.getLeft(),bb.getTop(),bb.getLeft()+featureBb.getRight(),bb.getButton()));
            right.setBoundingBox(new BoundingBox(bb.getLeft()+featureBb.getRight(),bb.getTop(),bb.getRight(),bb.getButton()));
            System.out.println(left+ "  "+right);

        }else{
            left.setBoundingBox(new BoundingBox(bb.getLeft(),bb.getTop(),bb.getRight(),bb.getTop()+featureBb.getButton()));
            right.setBoundingBox(new BoundingBox(bb.getLeft(),bb.getTop()+featureBb.getButton(),bb.getRight(),bb.getButton()));
            System.out.println(left+ "  "+right);

        }
        return  left.insert(f);



    }


    public BoundingBox getBoundingBox(){
        return bb;
    }

    private boolean isLeaf(){
        return left==null && right ==null;
    }
}
