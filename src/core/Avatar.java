package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TETile;
import tileengine.Tileset;

public class Avatar {
    TETile avatar = Tileset.AVATAR;
    private Movement movement;
    private TETile[][] tiles;
    int x, y; // Represents the current x and y coordinates of the avatar on the grid.
    private long prevActionTimestamp;

    /**
     * Attempts to move the character by a specified deltaX and deltaY.
     * Moves only if the target position is within bounds and is a floor tile.
     *               Avatar Class Data Structures:
     * Position Tracking:
     * int x, y: Represents the current x and y coordinates of the avatar on the grid.
     * Movement Constraints:
     * boolean canMoveUp, canMoveDown, canMoveLeft, canMoveRight: Flags indicating whether the avatar can move in each direction based on the game state.
     * Villain Class Data Structures (alongside Avatar class):
     * Inherits or encapsulates similar data structures as the Avatar class.
     * Additional data structures:
     * PriorityQueue<Node> openSet: For A* algorithm, storing nodes to be evaluated.
     * HashSet<Node> closedSet: For A* algorithm, storing nodes that have been evaluated.
     * HashMap<Node, Double> gScore: For A* algorithm, storing the cost of getting from the start node to a given node.
     * HashMap<Node, Double> fScore: For A* algorithm, storing the total cost of getting from the start node to the goal node passing by a given node.
     * World Class Data Structures:
     * Grid Representation:
     * TETile[][] tiles: Represents the grid of tiles in the world.
     * boolean[][] grid: Represents the walkable/non-walkable areas in the world.
     * Rooms:
     * ArrayDeque<Room> rooms: Collection of rooms generated in the world.
     * Player and Villain:
     * Avatar playerAvatar: Instance representing the player's avatar.
     * Villain villainAvatar: Instance representing the villain's avatar.
     * Other Important Aspects/Data Structures:
     * A Pathfinding (Utility Class):*
     * Node: Represents a node in the A* algorithm with coordinates, cost, and references to parent nodes.
     * AStarPathfinding: Utility class containing A* pathfinding logic/methods used by the villain.
     */

    public Avatar(int x, int y, TETile[][] TILES, int WIDTH, int HEIGHT) {
        this.x = x;
        this.y = y;
        tiles = TILES;

        movement = new Movement(WIDTH, HEIGHT, this);

        // are u sure x and y are the right ones?
        drawAvatar();
    }

    void drawAvatar() {
        tiles[x][y] = avatar;
    }
    static void draw(Avatar you, TETile[][] tiles, int x, int y) {
        tiles[x][y] = you.avatar;
    }

    public int getXPosition() {
        return x;
    }

    public int getYPosition() {
        return y;
    }

    public void setXPosition(int newX) {
        this.x = newX;
    }

    public void setYPosition(int newY) {
        this.y = newY;
    }

    public TETile[][] getTiles() {
        return tiles;
    }

    public void moveLeft() {
        clearPreviousPosition();
        movement.tryMove(-1, 0);
    }

    public void moveRight() {
        if (movement.isValidMove(x + 1, y)) {
            clearPreviousPosition();
            movement.tryMove(1, 0);
            //setXPosition(x + 1);
        }
    }

    public void moveUp() {
        if (movement.isValidMove(x, y + 1)) {
            clearPreviousPosition();
            movement.tryMove(0, 1);
            //setYPosition(y + 1);
        }
    }

    public void moveDown() {
        if (movement.isValidMove(x, y - 1)) {
            clearPreviousPosition();
            movement.tryMove(0, -1);
            //setYPosition(y - 1);
        }
    }

    private void clearPreviousPosition() {
        if (tiles[x][y] == Tileset.PATH) {
            tiles[x][y] = Tileset.PATH; // If the previous tile was a path tile, keep it as a path tile
        } else {
            tiles[x][y] = Tileset.FLOOR; // If the previous tile was a floor tile or any other, make it a floor tile
        }    }


}
