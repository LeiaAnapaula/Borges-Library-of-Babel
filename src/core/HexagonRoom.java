package core;
import tileengine.TETile;

public class HexagonRoom extends Room {
    public HexagonRoom(int row, int tile, int width, int height, TETile[][] tiles) {
        super(row, tile, width, height, tiles);
    }

    void fillWorld(TETile[][] tiles, TETile blank, TETile wall, TETile floor, TETile book) {
        int size = width / 2; // Assuming size is half of the width

        // Calculate the coordinates of the corners of the hexagon
        int x1 = centerXValue - size;
        int y1 = centerYValue;
        int x2 = centerXValue - size / 2;
        int y2 = row;
        int x3 = centerXValue + size / 2;
        int y3 = row;
        int x4 = centerXValue + size;
        int y4 = centerYValue;
        int x5 = centerXValue + size / 2;
        int y5 = row + height;
        int x6 = centerXValue - size / 2;
        int y6 = row + height;

        // Fill in the hexagon border
        drawLine(tiles, x1, y1, x2, y2, wall);
        drawLine(tiles, x2, y2, x3, y3, wall);
        drawLine(tiles, x3, y3, x4, y4, wall);
        drawLine(tiles, x4, y4, x5, y5, wall);
        drawLine(tiles, x5, y5, x6, y6, wall);
        drawLine(tiles, x6, y6, x1, y1, wall);

        // Fill the interior of the hexagon
        for (int currentRow = row + 1; currentRow < row + height; currentRow++) {
            fillRow(tiles, blank, floor, wall, currentRow);
        }
    }

    // Helper method to draw a line between two points
    private void drawLine(TETile[][] tiles, int x1, int y1, int x2, int y2, TETile tile) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = (x1 < x2) ? 1 : -1;
        int sy = (y1 < y2) ? 1 : -1;
        int err = dx - dy;

        while (true) {
            tiles[x1][y1] = tile;

            if (x1 == x2 && y1 == y2) {
                break;
            }

            int e2 = 2 * err;
            if (e2 > -dy) {
                err = err - dy;
                x1 = x1 + sx;
            }
            if (e2 < dx) {
                err = err + dx;
                y1 = y1 + sy;
            }
        }
    }

    // Fills the inside of the HexagonRoom after the border has been filled in.
    private void fillRow(TETile[][] tiles, TETile blank, TETile floor, TETile wall, int row) {
        boolean insideHexagon = false;
        for (int tile = this.tile; tile < this.tile + this.width; tile++) {
            if (tiles[tile][row] == wall) {
                insideHexagon = !insideHexagon;
            } else if (insideHexagon) {
                tiles[tile][row] = floor;
            }
        }
    }
}
