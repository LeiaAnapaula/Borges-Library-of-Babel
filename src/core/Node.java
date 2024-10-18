package core;

import java.awt.Point;

public class Node {
    private Point position;
    private int gCost; // Cost from the start node
    private int hCost; // Cost to the target node
    private int fCost;
    private Node parent;

    public Node(Point position, Node parent) {
        this.position = position;
        this.parent = parent;
        this.gCost = 0;
        this.hCost = 0;
        this.fCost = 0;
    }

    public Point getPosition() {
        return position;
    }

    public int getGCost() {
        return gCost;
    }

    public void setGCost(int gCost) {
        this.gCost = gCost;
    }

    public int getHCost() {
        return hCost;
    }

    public void setHCost(int hCost) {
        this.hCost = hCost;
    }

    public int getFCost() {
        return gCost + hCost;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }
}


//
//public class Node {
//    int x, y; // Coordinates of the node on the grid
//    int gCost; // Cost of getting from the starting node to this node
//    int hCost; // Estimated cost from this node to the target node (heuristic)
//    int fCost; // Total estimated cost (fCost = gCost + hCost)
//    Node parent; // A reference to the previous node in the optimal path
//
//    // Constructor
//    public Node(int x, int y) {
//        this.x = x;
//        this.y = y;
//        this.gCost = 0; // Initialize with a large value as an indicator
//        this.fCost = 0; // same. Will later be updated with new f cost.
//        this.parent = null; // Initially, there is no parent
//    }
//    public void updateFCost() {
//        this.fCost = this.gCost + this.hCost;
//    }
//}
