package logics.renderTools;

public class BoundingBox {
    public  enum Fitting {SMALL,BIG,PERFECT}
    private float right;
    private float bottom;
    private float top=0;
    private float left=0;

    public float getTop() {
        return top;
    }

    public void setTop(float top) {
        this.top = top;
    }

    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        this.left = left;
    }

    public BoundingBox(float width, float deep){
        this.right=deep;
        this.bottom = width;

    }


    public BoundingBox( float left,float top,float right, float bottom ) {


        this.right = right;
        this.bottom = bottom;
        this.top = top;
        this.left = left;
    }

    public Fitting isFit(BoundingBox bb){
        if ((this.getHeight() == bb.getHeight()) && (this.getWidth() == bb.getWidth())) return Fitting.PERFECT;
        if ((this.getHeight() >= bb.getHeight()) && (this.getWidth() >= bb.getWidth())) return Fitting.BIG;
        return  Fitting.SMALL;


    }


    public boolean isIntersect(BoundingBox bb1){

        return !(bb1.left > right
                || bb1.right < left
                || bb1.top > bottom
                || bb1.bottom < top);

    }
    public float getRight(){
        return right;
    }
    public float getButton(){
        return bottom;
    }

    public float getWidth() {
        return right-left;
    }


    public float getHeight() {
        return bottom-top;
    }


    public Point3d getCenter(){
//        return new Point3d(((this.getRight()-this.getLeft())/2)+this.getLeft(),0,((this.getButton()-this.getTop())/2)+this.getTop());
        return new Point3d(top,0,left);

    }

    public String toString(){
        return "Top: "+getTop()+" Left:"+getLeft()+" Right:"+this.getRight()+" Button:"+this.getButton() + " Center:"+getCenter().toString();
    }
}
