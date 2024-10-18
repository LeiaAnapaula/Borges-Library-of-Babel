package core;
import tileengine.TETile;

public class CircleRoom extends Room {
    public CircleRoom(int row, int tile, int width, int height, TETile[][] tiles) {
        super(row, tile, width, height, tiles);
    }

    @Override
    void fillWorld(TETile[][] tiles, TETile blank, TETile wall, TETile floor, TETile book) {
        int centerX = tile + width / 2;
        int centerY = row + height / 2;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int distanceSquared = (x - width / 2) * (x - width / 2) + (y - height / 2) * (y - height / 2);
                int radiusSquared = width / 2 * width / 2;

                if (distanceSquared <= radiusSquared) {
                    tiles[tile + x][row + y] = floor;
                }
            }
        }
        this.fillBorder(tiles, blank, wall, floor);
    }

    private void fillBorder(TETile[][] tiles, TETile blank, TETile wall, TETile floor) {
        for (int x = tile; x < tile + width; x++) {
            for (int y = row; y < row + height; y++) {
                if (tiles[x][y] == floor && checkNeighbors(x, y, tiles, blank)) {
                    tiles[x][y] = wall;
                }
            }
        }
    }

    /*
    We know that there's going to be a wall provided that at least one of a tile's neighbors is blank.
    We're going to exploit that fact now.
    Returns true if at least one neighbor is blank or doesn't exist.
     */
    private boolean checkNeighbors(int x, int y, TETile[][] tiles, TETile blank) {
        // Check if the coordinates are outside the shape boundaries
        if (x + 1 >= tile + width || x - 1 < tile || y + 1 >= row + height || y - 1 < row) {
            return true;
        }

        // Check if any neighbor is blank
        boolean firstCheck = tiles[x + 1][y].equals(blank) || tiles[x - 1][y].equals(blank);
        boolean secondCheck = tiles[x][y + 1].equals(blank) || tiles[x][y - 1].equals(blank);
        return firstCheck || secondCheck;
    }
}
