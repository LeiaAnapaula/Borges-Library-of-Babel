package core;

import tileengine.TETile;

import java.util.*;

public class Room {
    int row;
    int tile;
    int width;
    int height;
    int centerXValue; // Updated name to avoid hiding the field centerX
    int centerYValue; // Updated name to avoid hiding the field centerY
    private TETile[][] roomTiles; // Updated name to avoid hiding the field tiles
    private static int counter = 0;
    private int roomID;
    ArrayDeque<Room> connectedRooms;
    String book;

    public Room(int row, int tile, int width, int height, TETile[][] tiles) {
        this.row = row;
        this.tile = tile;
        this.width = width;
        this.height = height;
        centerXValue = calculateCenterX();
        centerYValue = calculateCenterY();
        this.roomTiles = tiles;
        this.roomID = counter++;
        connectedRooms = new ArrayDeque<>();
    }

    void fillWorld(TETile[][] tiles, TETile blank, TETile floor, TETile wall, TETile book) {
    }

    // gotta be an instance method. Specific to a single room.
    int calculateCenterX() {
        int centerOfShape;
        if (this.width % 2 == 0) {
            centerOfShape = this.width / 2;
        } else {
            centerOfShape = (this.width - 1) / 2;
        }
        return centerOfShape + this.tile; // the upper left tile
    }

    int calculateCenterY() {
        int centerOfShape;
        if (this.height % 2 == 0) {
            centerOfShape = this.height / 2;
        } else {
            centerOfShape = (this.height - 1) / 2;
        }
        return centerOfShape + this.row;
    }

    public void fillBookshelf(TETile book) {
        roomTiles[centerXValue][centerYValue] = book;
    }

    // Adds two newly connected rooms to each other's lists of connected rooms.
    void connect(Room room) {
        this.connectedRooms.add(room);
        room.connectedRooms.add(this);
        System.out.println(room.getID());
        System.out.println(this.connectedRooms);
        System.out.println(room.connectedRooms);
    }

    int getID() {
        return roomID;
    }

    public void setTile(int centerX, int centerY, TETile shelf) {
        roomTiles[centerX][centerY] = shelf;
    }

    public boolean contains(int endX, int endY) {
        // this contains method may be the problem
        boolean checkX = endX >= this.tile && endX < this.tile + this.width;
        boolean checkY = endY >= this.row && endY < this.row + this.height;

        return checkX && checkY;
    }

    public void fillBook(String book) {
        this.book = book;
    }

    public String getBook() {
        return book;
    }

}
