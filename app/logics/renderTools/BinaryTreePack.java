package logics.renderTools;

import interfaces.Component;

/**
 * Created by bedux on 29/02/16.
 */
public class BinaryTreePack {

    private Node root;

    public BinaryTreePack(Component rootCP) {
        root = new Node(rootCP.getFeatures().getBoundingBox());
    }


    /***
     *
     * @param component Insert the component
     */
    public void insert(Component component) {
        Node toInsert = root.insert(component);
        if (toInsert != null) {
            toInsert = toInsert.split(component);
            toInsert.assign(component);
        } else {
            resize(component);
            Node n = root.insert(component);
            if (n != null) {
                n = n.split(component);
                n.assign(component);
            }
        }


    }

    /***
     *
     * @param c resize the given component
     * @return  teh root node
     */
    public Node resize(Component c) {
        BoundingBox target = c.getFeatures().getBoundingBox();

        boolean cantGoButton = target.getWidth() <= root.getBoundingBox().getWidth();
        boolean cantGoRight = target.getDepth() <= root.getBoundingBox().getDepth();

        boolean shouldGoRight = cantGoRight && ((root.getBoundingBox().getWidth() + target.getWidth()) < root.getBoundingBox().getDepth());
        boolean shouldGoButton = cantGoButton && ((root.getBoundingBox().getDepth() + target.getDepth()) < root.getBoundingBox().getWidth());


        if (shouldGoRight) {
            return growRight(target);
        } else if (shouldGoButton) {
            return growBottom(target);

        } else if (cantGoRight) {
            return growRight(target);
        } else if (cantGoButton) {
            return growBottom(target);

        } else return null;


    }

    /**
     * Grow Bottom by the given size
     * @param boxSize
     * @return
     */
    public Node growBottom(BoundingBox boxSize) {
        Node helper = root;
        root = new Node(new BoundingBox(
                0,
                0,
                root.getBoundingBox().getWidth(),
                root.getBoundingBox().getButton() + boxSize.getDepth()));


        root.setbottom(new Node(new BoundingBox(
                0,
                helper.getBoundingBox().getDepth(),
                helper.getBoundingBox().getWidth(),
                boxSize.getDepth(), 1
        )));

        root.setRight(helper);

        root.assign();
        return root;
    }

    /**
     * Grow right by the given size
     * @param boxSize the box size
     * @return
     */
    public Node growRight(BoundingBox boxSize) {
        Node helper = root;
        root = new Node(new BoundingBox(0,
                0,
                root.getBoundingBox().getRight() + boxSize.getWidth(),
                root.getBoundingBox().getDepth()));

        root.setRight(new Node(new BoundingBox(
                helper.getBoundingBox().getWidth(),
                0,
                boxSize.getWidth(),
                helper.getBoundingBox().getDepth(), 1)));

        root.setbottom(helper);

        root.assign();
        return root;

    }
}
