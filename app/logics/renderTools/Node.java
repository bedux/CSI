package logics.renderTools;

import interfaces.Component;

/**
 * Created by bedux on 29/02/16.
 */
public class Node {
    private Component component = null;

    private Node left = null;

    private Node bottom = null;

    private boolean occupaid = false;
    private BoundingBox boundingBox;

    public Node(BoundingBox bb) {
        this.boundingBox = bb;
    }

    public Node getRight() {
        return left;
    }

    public void setRight(Node right) {
        left = right;
    }

    public Node getBottom() {
        return bottom;
    }

    public BoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    public void setbottom(Node bottom) {
        this.bottom = bottom;
    }

    public Node insert(Component component) {
        if (!isEmpty()) {
            Node retur = null;
            if (left != null) {
                retur = left.insert(component);
            }
            if (bottom != null && retur == null) {
                retur = bottom.insert(component);

            }
            return retur;
        } else if ((this.getBoundingBox().isFit(component.getFeatures().getBoundingBox()) != BoundingBox.Fitting.SMALL)) {
            return this;
        } else {
            return null;
        }
    }

    public Node split(Component component) {
        //orizontal splitt


        left = new Node(new BoundingBox(boundingBox.getLeft() + component.getFeatures().getBoundingBox().getWidth(),
                boundingBox.getTop(),
                boundingBox.getWidth() - component.getFeatures().getBoundingBox().getWidth(),
                component.getFeatures().getBoundingBox().getDeep(), 1));

        bottom = new Node(new BoundingBox(boundingBox.getLeft(),
                boundingBox.getTop() + component.getFeatures().getBoundingBox().getDeep(),
                boundingBox.getWidth(),
                boundingBox.getDeep() - component.getFeatures().getBoundingBox().getDeep(), 1));

        boundingBox = (new BoundingBox(boundingBox.getLeft(),
                boundingBox.getTop(),
                boundingBox.getLeft() + component.getFeatures().getBoundingBox().getWidth(),
                boundingBox.getTop() + component.getFeatures().getBoundingBox().getDeep()));

        return this;
    }

    public void assign(Component component) {


        component.getFeatures().getBoundingBox().setBB(this.boundingBox);
        this.component = component;
        occupaid = true;
    }

    public void assign() {
        occupaid = true;
    }

    private boolean isEmpty() {
        return !occupaid;
    }


}
