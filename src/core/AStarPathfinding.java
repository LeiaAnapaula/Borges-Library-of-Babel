package core;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import tileengine.TETile;
public class AStarPathfinding {
    private TETile[][] world;
    private int width;
    private int height;
    Node node = new Node(new Point(0, 0), null);

    public AStarPathfinding(TETile[][] world) {
        this.world = world;
        this.width = world.length;
        this.height = world[0].length;
    }

    // Implement A* algorithm to find the shortest path from start to target
    public List<Point> findPath(Point start, Point target) {
        // Initialize lists for open and closed nodes
        List<Node> openSet = new ArrayList<>();
        List<Node> closedSet = new ArrayList<>();

        // Create the start node and add it to the open set
        Node startNode = new Node(start, null);
        openSet.add(startNode);

        // Loop until the open set is empty
        while (!openSet.isEmpty()) {
            // Find the node in the open set with the lowest f cost
            Node currentNode = findLowestFCostNode(openSet);

            // If the current node is the target, reconstruct and return the path
            if (currentNode.getPosition() != null && currentNode.getPosition().equals(target)) {
                return reconstructPath(currentNode);
            }


            // Move the current node from the open set to the closed set
            openSet.remove(currentNode);
            closedSet.add(currentNode);

            // Generate neighboring nodes
            List<Node> neighbors = getNeighbors(currentNode, target);

            for (Node neighbor : neighbors) {
                if (closedSet.contains(neighbor)) {
                    continue; // Skip the neighbor if it is already in the closed set
                }

                int tentativeGCost = currentNode.getGCost() + calculateDistance(currentNode, neighbor);
                if (!openSet.contains(neighbor) || tentativeGCost < neighbor.getGCost()) {
                    neighbor.setGCost(tentativeGCost);
                    neighbor.setHCost(calculateDistance(neighbor, new Node(target, null)));
                    neighbor.setParent(currentNode);

                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }

        // No path found
        return new ArrayList<>();
    }

    // Helper method to find the node in the open set with the lowest f cost
    private Node findLowestFCostNode(List<Node> openSet) {
        Node lowestCostNode = openSet.get(0);
        for (Node node : openSet) {
            if (node.getFCost() < lowestCostNode.getFCost()) {
                lowestCostNode = node;
            }
        }
        return lowestCostNode;
    }

    // Helper method to reconstruct the path from the target node to the start node
    private List<Point> reconstructPath(Node currentNode) {
        List<Point> path = new ArrayList<>();
        while (currentNode != null) {
            path.add(currentNode.getPosition());
            currentNode = currentNode.getParent();
        }
        return path;
    }

    // Helper method to calculate the distance between two nodes (using Manhattan distance)
    private int calculateDistance(Node nodeA, Node nodeB) {
        int xDistance = Math.abs(nodeA.getPosition().x - nodeB.getPosition().x);
        int yDistance = Math.abs(nodeA.getPosition().y - nodeB.getPosition().y);
        return xDistance + yDistance;
    }

    // Helper method to get neighboring nodes
    private List<Node> getNeighbors(Node node, Point target) {
        List<Node> neighbors = new ArrayList<>();
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } }; // Adjacent directions

        for (int[] dir : directions) {
            int neighborX = node.getPosition().x + dir[0];
            int neighborY = node.getPosition().y + dir[1];
            Point neighborPos = new Point(neighborX, neighborY);

            if (isValidNeighbor(neighborPos)) {
                Node neighborNode = new Node(neighborPos, null);
                neighbors.add(neighborNode);
            }
        }
        return neighbors;
    }

    // Helper method to check if a neighbor position is valid
    private boolean isValidNeighbor(Point position) {
        int x = position.x;
        int y = position.y;
        return x >= 0 && x < width && y >= 0 && y < height && world[x][y] != null;
    }
}
