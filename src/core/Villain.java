package core;

import java.util.*;
import tileengine.TETile;
import tileengine.Tileset;

import java.util.Arrays;
import java.util.Comparator;

public class Villain {
    private int x;
    private int y;
    private TETile[][] tiles;
    private TETile villainTile = Tileset.VILLAIN;
    private TETile path = Tileset.PATH;
    private int width;
    private int height;
    private Avatar avatar;
    private PriorityQueue<Point> pq;
    private ArrayList<Point> pathToAvatar = new ArrayList<>();;
    private int[][] distances;
    private int[] dxVals;
    private int[] dyVals;
    public Villain(int x, int y, TETile[][] tiles, int width, int height, Avatar avatar) {
        this.x = x;
        this.y = y;
        this.tiles = tiles;
        this.width = width;
        this.height = height;
        this.avatar = avatar;
    }
    // represent all the floor tiles as numbers
    // run a naive Dijkstra algorithm with priority queue
    // move the villain to the next tile in the path

    // check phone picture

    public void findPathToAvatar() {
        distances = new int[width][height];
        dxVals = new int[]{0, 0, -1, 1};
        dyVals = new int[]{1, -1, 0, 0};

        // Initialize all distances to infinite
        for (int i = 0; i < width; i++) {
            Arrays.fill(distances[i], Integer.MAX_VALUE);
        }

        pq = new PriorityQueue<>(Comparator.comparingInt(a -> distances[a.x][a.y]));
        distances[x][y] = 0;
        pq.offer(new Point(avatar.getXPosition(), avatar.getYPosition()));

        Point[][] parent = new Point[width][height];

        while (!pq.isEmpty()) {
            Point curr = pq.poll();
            for (int i = 0; i < 4; i++) {
                int nx = curr.x + dxVals[i];
                int ny = curr.y + dyVals[i];

                if (nx >= 0 && nx < width && ny >= 0 && ny < height
                        && (tiles[nx][ny] == Tileset.FLOOR || tiles[nx][ny] == Tileset.PATH) && distances[nx][ny] > distances[curr.x][curr.y] + 1) {
                    distances[nx][ny] = distances[curr.x][curr.y] + 1;
                    pq.offer(new Point(nx, ny));
                    parent[nx][ny] = curr;
                }
            }
        }

        // current error: villain moves by two tiles when getting closer to avatar.
        clearPath();
        pathToAvatar = new ArrayList<>();

        Point nextMove = null;
        int minDistance = Integer.MAX_VALUE; // Set an initial minimum distance value

        // potential error with temp management
        //TEMPXY = POS OF VILLAIN
        int tempx = x;
        int tempy = y;
        //WHILE NOT LOSE
        while (!(Math.abs(avatar.getXPosition() - tempx) <= 1 && Math.abs(avatar.getYPosition() - tempy) <= 1)) {
            //ITERATE THRU VALS - FOR SOME REASON DOUBLE?
            for (int i = 0; i < 4; i++) {
                int nx = tempx + dxVals[i];
                int ny = tempy + dyVals[i];
                //IF IN BOUNDS AND DISTANCE IS LESS THAN MIN DISTANCE AND TILE IS FLOOR OR PATH
                if (nx >= 0 && nx < width && ny >= 0 && ny < height && distances[nx][ny] < minDistance
                        && (tiles[nx][ny] == Tileset.FLOOR || tiles[nx][ny] == Tileset.PATH)) {
                    minDistance = distances[nx][ny];
                    nextMove = new Point(nx, ny);
                    pathToAvatar.add(nextMove);
                }
            }
            if (nextMove != null && minDistance < distances[avatar.getXPosition()][avatar.getYPosition()]) { // minDistance < distances[avatar.getXPosition()][avatar.getYPosition()]
                tempx = nextMove.x; // Update the villain's x-coordinate from nextMove
                tempy = nextMove.y; // Update the villain's y-coordinate from nextMove
            }
        }

//        Point curr = new Point(x, y);
//        while (curr != null && distances[curr.x][curr.y] != 0) {
//            pathToAvatar.add(curr);
//            curr = parent[curr.x][curr.y];
//        }
//        pathToAvatar.add(curr);
//        Collections.reverse(pathToAvatar);

//        for (Point ele : pathToAvatar) {
//            System.out.println(ele.x);
//            System.out.println("x is top, y is bottom");
//            System.out.println(ele.y);
//        }

    }

    private void clearPath() {
        for (Point tile : pathToAvatar) {
            if (tiles[tile.x][tile.y] == Tileset.PATH) {
                tiles[tile.x][tile.y] = Tileset.FLOOR;
            }
        }

    }
    public void moveVillain() {

        // check if the first 2 elements of the path to avatar touch the villain

        //findPathToAvatar(); // Find the path again (or use the previously calculated path)
        int starter = 0;
        int counter = 0;
        for (int i = 0; i < 4; i++) {
            int nx = x + dxVals[i];
            int ny = y + dyVals[i];
            Point first = pathToAvatar.get(0);
            if (first.x == nx && first.y == ny){
                counter += 1;
            }
            Point second = pathToAvatar.get(1);
            if (second.x == nx && second.y == ny){
                counter += 1;
            }
            if (counter > 1){
                starter = 1;
            }
        }
        // build it from the last
        //THIS IS CHANGED
        Point nextMove = pathToAvatar.get(starter); // first element is sometimes

        System.out.println("PATH");
        System.out.println("CURRPOS: " + x + " ," + y);
        System.out.println("NEXTPOS: " + nextMove.x + " ," + nextMove.y);
        for (Point p : pathToAvatar) {
            if (p.x == x && p.y == y){
                System.out.println("CURRPOS IN LIST");
            }
        }
        for (Point p : pathToAvatar) {
            System.out.println("POSTTION: " + p.x + " ," + p.y + ", DISTANCE: " + distances[p.x][p.y]);
        }
//        PriorityQueue<Wrapper<Object>> pq = new PriorityQueue<>(Comparator.comparingInt(Wrapper::getPriority));
//        for (Point p : pathToAvatar) {
//            if (distances[p.x][p.y] > 0 &&  p.x != x && p.y != y) {
//                pq.offer(new Wrapper<>(p, distances[p.x][p.y]));
//        }
//        }
//        if (pq.isEmpty()) {
//            return;
//        }
//        Point nextMove = pq.poll().getObject();
        // either get first or last elem
        // while we dont reach the avatar

        if (nextMove != null && (tiles[nextMove.x][nextMove.y] == Tileset.FLOOR || tiles[nextMove.x][nextMove.y] == Tileset.PATH)) {
            // Clear the villain's current position
            tiles[x][y] = Tileset.FLOOR;

            x = nextMove.x; // Update the villain's x-coordinate from nextMove
            y = nextMove.y; // Update the villain's y-coordinate from nextMove

            // Update the villain's new position on the tiles grid
            tiles[x][y] = villainTile; // Assuming villainTile represents the villain's tile
        }
    }


    public void drawVillain() {
        tiles[x][y] = villainTile;
    }
    public void drawPath() {
        int starter = 0;
        int counter = 0;
        for (int i = 0; i < 4; i++) {
            int nx = x + dxVals[i];
            int ny = y + dyVals[i];
            // check if the first 2 elements of the path to avatar touch the villain
            // then, get the index-0 element
            Point first = pathToAvatar.get(0);
            if (first.x == nx && first.y == ny){
                counter += 1;
            }
            // treat the index-1 element different.
            Point second = pathToAvatar.get(1);
            if (second.x == nx && second.y == ny){
                counter += 1;
            }
            if (counter > 1){
                starter = 1;
            }
        }
        for (Point p : pathToAvatar) {
            tiles[p.x][p.y] = path;
        }
        if (starter == 1){
            // this deletes the previous red tile
            tiles[pathToAvatar.get(0).x][pathToAvatar.get(0).y] = Tileset.FLOOR;
        }
    }
    public int getXPosition() {
        return x;
    }

    public int getYPosition() {
        return y;
    }

    // Helper class for coordinates
    private static class Point {
        int x;
        int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    //WRAPPER FN
    private class Wrapper<T> {
        private Point pt;
        private int pri;

        public Wrapper(Point object, int priority) {
            this.pt = object;
            this.pri = priority;
        }

        public Point getObject() {
            return pt;
        }

        public int getPriority() {
            return pri;
        }
    }

}