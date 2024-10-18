package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.util.ArrayDeque;

public class Hallways {
    private final ArrayDeque<Room> rooms;
    private final TETile floorTile = Tileset.FLOOR;
    private final TETile wallTile = Tileset.FLOWER;
    private final TETile blankTile = Tileset.NOTHING;
    private final int WIDTH;
    private final int HEIGHT;
    private final TETile[][] tiles;
    private final boolean[][] grid;

    public Hallways(ArrayDeque<Room> roomList, TETile[][] roomTiles, int width, int height, boolean[][] roomGrid) {
        this.rooms = roomList;
        this.tiles = roomTiles;
        this.WIDTH = width;
        this.HEIGHT = height;
        this.grid = roomGrid;
    }

    public void createHallway(ArrayDeque<Room> roomList) {
        for (Room room : roomList) {
            connectRoom(room);
        }
    }

    private void connectRoom(Room currentRoom) {
        int centerX = currentRoom.calculateCenterX();
        int centerY = currentRoom.calculateCenterY();
        connectInDirection(currentRoom, centerX + (currentRoom.width / 2), centerY, 1, 0); // Right
        connectInDirection(currentRoom, centerX - (currentRoom.width / 2), centerY, -1, 0); // Left
        connectInDirection(currentRoom, centerX, centerY + (currentRoom.height / 2), 0, 1); // Up
        connectInDirection(currentRoom, centerX, centerY - (currentRoom.height / 2), 0, -1); // Down
    }

    private void connectInDirection(Room currentRoom, int startX, int startY, int dx, int dy) {
        int endX = startX + dx;
        int endY = startY + dy;

        while (endX >= 0 && endX < WIDTH && endY >= 0 && endY < HEIGHT) {
            if (grid[endX][endY]) {
                Room room = findRoom(endX, endY);
                if (room != null) {
                    // don't draw a hallway until find a room
                    drawHallway(currentRoom, room);
                    break;
                }
            }
            endX += dx;
            endY += dy;
        }
    }

    private Room findRoom(int endX, int endY) {
        for (Room room : rooms) {
            if (room.contains(endX, endY)) {
                return room;
            }
        }
        return null;
    }

    private void drawHallway(Room currentRoom, Room room) {
        int startX = currentRoom.calculateCenterX();
        int startY = currentRoom.calculateCenterY();
        int endX = room.calculateCenterX();
        int endY = room.calculateCenterY();

        if (startX == endX) {
            drawVerticalHallway(startX, startY, endY);
        } else if (startY == endY) {
            drawHorizontalHallway(startY, startX, endX);
        } else {
            drawLHallway(startX, startY, endX, endY);
        }
    }

    private void drawLHallway(int startX, int startY, int endX, int endY) {
        int dx = (endX - startX) / Math.abs(endX - startX);
        int dy = (endY - startY) / Math.abs(endY - startY);
        int x = startX;
        int y = startY;
        while (x != endX) {
            tiles[x][y] = floorTile;
            placeWalls(x, y);
            x += dx;
        }
        while (y != endY) {
            tiles[x][y] = floorTile;
            placeWalls(x, y);
            y += dy;
        }
    }

    private void drawHorizontalHallway(int startY, int startX, int endX) {
        int dx = (endX - startX) / Math.abs(endX - startX);
        for (int x = startX; x != endX; x += dx) {
            tiles[x][startY] = floorTile;
            placeWalls(x, startY);
        }
    }

    private void drawVerticalHallway(int startX, int startY, int endY) {
        int dy = (endY - startY) / Math.abs(endY - startY);
        for (int y = startY; y != endY; y += dy) {
            tiles[startX][y] = floorTile;
            placeWalls(startX, y);
        }
    }

    private void placeWalls(int x, int y) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int nx = x + dx;
                int ny = y + dy;
                if (nx >= 0 && nx < WIDTH && ny >= 0 && ny < HEIGHT) {
                    if (tiles[nx][ny] == blankTile) {
                        tiles[nx][ny] = wallTile;
                    }
                }
            }
        }
    }
}
