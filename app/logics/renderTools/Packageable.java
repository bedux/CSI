package logics.renderTools;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by bedux on 27/02/16.
 */
@JsonIgnoreProperties
public abstract class Packageable implements Serializable{
    @JsonIgnore public  float gap = 80;
    @JsonIgnore private float height;
    @JsonIgnore private float[] color;
    @JsonIgnoreProperties private int segment;
    @JsonIgnoreProperties private BoundingBox bb = new BoundingBox(0,0);


    public float getRendererWidth(){
        return bb.getWidth()-gap/2;
    }

    public float getRendererDeep(){
        return bb.getDeep()-gap/2;

    }

    public float getRendererTop(){
        return bb.getTop()+gap/2;
    }

    public float getRendererLeft(){
        return bb.getLeft()+gap/2;

    }

    public float getWidth() {
        return bb.getWidth();
    }

    public void setWidth(float width) {
        bb.setWidth(width+gap);
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getDeep() {
        return bb.getDeep();
    }

    public void setDeep(float deep) {
        bb.setDeep(deep+gap);
    }

    public Packageable(float width, float height, float deep) {

        this.height = height;
        bb = new BoundingBox(width,deep);
    }

    public Packageable(){};

    public BoundingBox getBoundingBox(){
        return  bb;
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
        this.height = height;
        this.color = color;
        this.segment = segment;
        bb = new BoundingBox(width,deep);


    }
    protected abstract void  bindingToPakageble();

    @Override
    public String toString() {
        return "Packageable{" +
                ", height=" + height +
                ", color=" + Arrays.toString(color) +
                ", segment=" + segment +
                ", bb=" + bb +
                '}';
    }
}
