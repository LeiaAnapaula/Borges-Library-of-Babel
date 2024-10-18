package core;

import tileengine.TETile;

public class RectangleRoom extends Room {

    public RectangleRoom(int row, int tile, int width, int height, TETile[][] tiles) {
        super(row, tile, width, height, tiles);
    }

    @Override
    void fillWorld(TETile[][] tiles, TETile blank, TETile wall, TETile floor, TETile book) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (findWall(x, y)) {
                    tiles[tile + x][row + y] = wall;
                } else {
                    tiles[tile + x][row + y] = floor;
                }
            }
        }
    }

    /*
    As the borders of the wall have certain x or y coordinates, this function takes in a series of
    coordinates (x,y) and determines whether it is a wall or not.
    */
    private boolean findWall(int x, int y) {
        boolean isLeftSide = x == 0;
        boolean isRightSide = x == this.width - 1;
        boolean isBottomSide = y == 0;
        boolean isTopSide = y == this.height - 1;
        return isLeftSide || isRightSide || isBottomSide || isTopSide;
    }
}
