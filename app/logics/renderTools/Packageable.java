package logics.renderTools;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by bedux on 27/02/16.
 */
@JsonIgnoreProperties
public abstract class Packageable {
    @JsonIgnore
    public float gap = 40;
    @JsonIgnore
    private float height;
    @JsonIgnore
    private float color;
    @JsonIgnore
    private int buildingType;
    @JsonIgnore
    private BoundingBox bb = new BoundingBox(0, 0);


    public Packageable(float width, float height, float deep) {

        this.height = height;
        bb = new BoundingBox(width, deep);
    }

    public Packageable() {
    }

    public Packageable(float width, float height, float deep, float color, int buildingType) {
        this.height = height;
        this.buildingType = buildingType;
        bb = new BoundingBox(width, deep);


    }

    public float getRendererWidth() {

        return bb.getWidth() - gap;
    }

    public float getRendererDeep() {
        return bb.getDeep() - gap;

    }

    public float getRendererTop() {
        return bb.getTop() + gap / 2;
    }

    public float getRendererLeft() {
        return bb.getLeft() + gap / 2;

    }

    public float getWidth() {
        return bb.getWidth();
    }

    public void setWidth(float width) {
        bb.setWidth(width + gap);
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

    ;

    public void setDeep(float deep) {
        bb.setDeep(deep + gap);
    }

    public BoundingBox getBoundingBox() {
        return bb;
    }

    public float getColor() {
        return color;
    }

    public void setColor(float color) {
        this.color = color;
    }

    public int getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(int buildingType) {
        this.buildingType = buildingType;
    }

    protected abstract void bindingToPakageble();

    @Override
    public String toString() {
        return "Packageable{" +
                ", height=" + height +
                ", color=" + (color) +
                ", buildingType=" + buildingType +
                ", bb=" + bb +
                '}';
    }
}
