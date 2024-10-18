package knightworld;

import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

/**
 * Draws a world consisting of knight-move holes.
 */
public class KnightWorld {

    private TETile[][] tiles;

    public KnightWorld(int width, int height, int holeSize) {
        tiles = new TETile[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = Tileset.FLOWER;
            }
        }

        // get x and y recursively
        // start 0,0
        int initX = 0;
        int initY = 0;

        drawHole(initX, initY, holeSize);
        traverseHoles(initX, initY, holeSize);
        // BFS
        // Recursive function
    }

    public void traverseHoles(int x, int y, int holeSize) {
        // We may also need a condition for visited!! If there's alr hole, means visited
        // pass in a coordinate to determine if a Hole should be draw there
        // Set the tile to Tileset.NOTHING
        tiles[x][y] = Tileset.NOTHING;

        traverseHalf(x, y, holeSize);
        traverseOtherHalf(x, y, holeSize);

    }

    public void traverseHalf(int x, int y, int holeSize) {
        // Recursive calls for knight moves
        if (holeSize <= 0 || x < 0 || y < 0 || x >= tiles.length || y >= tiles[0].length) {
            return;
        }

        // draw and traverse the 1st hole
        drawHole(x + (1 * holeSize), y + (2 * holeSize), holeSize); // 2 up 1 right
        traverseHalf(x + (1 * holeSize), y + (2 * holeSize), holeSize);

        // draw and traverse the 2nd hole
        drawHole(x + (2 * holeSize), y - (1 * holeSize), holeSize); // 2 right 1 down
        traverseHalf(x + (2 * holeSize), y - (1 * holeSize), holeSize);
    }

    public void traverseOtherHalf(int x, int y, int holeSize) {
        if (holeSize <= 0 || x < 0 || y < 0 || x >= tiles.length || y >= tiles[0].length) {
            return;
        }

        // draw and traverse the 4th Hole
        drawHole(x - (2 * holeSize), y + (1 * holeSize), holeSize);
        traverseOtherHalf(x - (2 * holeSize), y + (1 * holeSize), holeSize);

        // draw and traver the 1st Hole
        drawHole(x + (1 * holeSize), y + (2 * holeSize), holeSize);
        traverseOtherHalf(x + (1 * holeSize), y + (2 * holeSize), holeSize);

        //traverseOtherHalf(x - (1 * holeSize), y - (2 * holeSize), holeSize);

    }

    public TETile drawHole(int x, int y, int holeSize) {
        // if i start x on x, then i just need to go up to x + holeSize and it'd be fine.
        for (int i = 0; i < holeSize; i++) {
            for (int j = 0; j < holeSize; j++) {
                int posX = x + i;
                int posY = y + j;

                // Check if the coordinates are within the bounds of the tile array
                if (posX >= 0 && posX < tiles.length && posY >= 0 && posY < tiles[0].length) {
                    tiles[posX][posY] = Tileset.NOTHING; // Set the tile to Tileset.NOTHING
                }
            }
        }

        return Tileset.NOTHING;
    }

    /** Returns the tiles associated with this KnightWorld. */
    public TETile[][] getTiles() {
        return tiles;
    }

    public static void main(String[] args) {
        // Change these parameters as necessary
        int width = 50;
        int height = 30;
        int holeSize = 3;

        KnightWorld knightWorld = new KnightWorld(width, height, holeSize);

        TERenderer ter = new TERenderer();
        ter.initialize(width, height);
        ter.renderFrame(knightWorld.getTiles());
    }
}
