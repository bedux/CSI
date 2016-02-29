package logics.renderTools;

import java.util.Arrays;

/**
 * Created by bedux on 27/02/16.
 */
public class Packageable {
    private float width;
    private float height;
    private float deep;
    private float[] color;
    private int segment;

    private BoundingBox bb;

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {

        this.width = width;
        bb = new BoundingBox(width,deep);

    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getDeep() {
        return deep;
    }

    public void setDeep(float deep) {
        this.deep = deep;
        bb = new BoundingBox(width,deep);

    }

    public Packageable(float width, float height, float deep) {

        this.width = width;
        this.height = height;
        this.deep = deep;
        bb = new BoundingBox(width,deep);
    }

    public Packageable(){};

    public BoundingBox getBoundingBox(){
        return  bb;
    }
    public void setBB(BoundingBox bb){
        this.bb = bb;
    }

    public float[] getColor() {
        return color;
    }

    public void setColor(float[] color) {
        this.color = color;
    }

    public int getSegment() {
        return segment;
    }

    public void setSegment(int segment) {
        this.segment = segment;
    }

    public Packageable(float width, float height, float deep, float[] color, int segment) {
        this.width = width;
        this.height = height;
        this.deep = deep;
        this.color = color;
        this.segment = segment;
        bb = new BoundingBox(width,deep);


    }

    @Override
    public String toString() {
        return "Packageable{" +
                "width=" + width +
                ", height=" + height +
                ", deep=" + deep +
                ", color=" + Arrays.toString(color) +
                ", segment=" + segment +
                ", bb=" + bb +
                '}';
    }
}
