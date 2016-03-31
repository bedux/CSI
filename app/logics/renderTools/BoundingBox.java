package logics.renderTools;

import java.io.Serializable;

public class BoundingBox implements Serializable {

    private float right;
    private float bottom;
    private float top = 0;
    private float left = 0;
    public BoundingBox(float width, float deep) {
        this.right = deep;
        this.bottom = width;
    }


    public BoundingBox(float left, float top, float right, float bottom) {


        this.right = right;
        this.bottom = bottom;
        this.top = top;
        this.left = left;
    }

    public BoundingBox(float left, float top, float width, float deep, int n) {


        this.top = top;
        this.left = left;
        this.right = left + width;
        this.bottom = top + deep;


    }

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

    public void setBB(BoundingBox bb) {
        this.right = bb.right;
        this.left = bb.left;
        this.top = bb.top;
        this.bottom = bb.bottom;

    }

    public Fitting isFit(BoundingBox bb) {
        if ((this.getDepth() == bb.getDepth()) && (this.getWidth() == bb.getWidth())) return Fitting.PERFECT;
        if ((this.getDepth() >= bb.getDepth()) && (this.getWidth() >= bb.getWidth())) return Fitting.BIG;
        return Fitting.SMALL;


    }

    public float getRight() {
        return right;
    }

    public float getButton() {
        return bottom;
    }

    public float getWidth() {
        return right - left;
    }

    public void setWidth(float w) {
        this.right = this.left + w;
    }

    public float getDepth() {
        return bottom - top;
    }

    public void setDepth(float d) {
        this.bottom = this.top + d;
    }

    public Point3d getCenter() {
        return new Point3d(((this.getRight() - this.getLeft()) / 2) + this.getLeft(), 0, ((this.getButton() - this.getTop()) / 2) + this.getTop());
//        return new Point3d(left,0,top);

    }

    public String toString() {
        return "Top: " + getTop() + " Left:" + getLeft() + " Right:" + this.getRight() + " Button:" + this.getButton();
    }

    public enum Fitting {SMALL, BIG, PERFECT}
}
