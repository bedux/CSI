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


    public void insert(Component component) {
        Node toInsert = root.insert(component);
        if (toInsert != null) {
            toInsert = toInsert.split(component);
            toInsert.assign(component);
//
//            component.getFeatures().setWidth(toInsert.getBoundingBox().getWidth());
//            component.getFeatures().setDepth(toInsert.getBoundingBox().getDepth());


        } else {
            resize(component);
            Node n = root.insert(component);
            if (n != null) {
                n = n.split(component);
                n.assign(component);

//                component.getFeatures().setWidth(n.getBoundingBox().getWidth());
//                component.getFeatures().setDepth(n.getBoundingBox().getDepth());

            } else {
            }


        }


    }

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

    public Node growBottom(BoundingBox bb) {
        Node helper = root;
        root = new Node(new BoundingBox(
                0,
                0,
                root.getBoundingBox().getWidth(),
                root.getBoundingBox().getButton() + bb.getDepth()));


        root.setbottom(new Node(new BoundingBox(
                0,
                helper.getBoundingBox().getDepth(),
                helper.getBoundingBox().getWidth(),
                bb.getDepth(), 1
        )));

        root.setRight(helper);

        root.assign();
        return root;
    }

    public Node growRight(BoundingBox bb) {
        Node helper = root;
        root = new Node(new BoundingBox(0,
                0,
                root.getBoundingBox().getRight() + bb.getWidth(),
                root.getBoundingBox().getDepth()));

        root.setRight(new Node(new BoundingBox(
                helper.getBoundingBox().getWidth(),
                0,
                bb.getWidth(),
                helper.getBoundingBox().getDepth(), 1)));

        root.setbottom(helper);

//        System.out.println(bb+ " current 1 ");
//        System.out.println(root.getBoundingBox()+ " current 1 ");
//        System.out.println(root.getBottom().getBoundingBox()+ " Button ");
//        System.out.println(root.getRight().getBoundingBox()+ " Right ");
        root.assign();
        return root;

    }
}
