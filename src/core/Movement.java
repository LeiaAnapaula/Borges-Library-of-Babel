package core;

import core.World;
import tileengine.TETile;
import tileengine.Tileset;

/**
 *  Provides the logic for movement of Tetris pieces.
 *
 *  @author Erik Nelson, Omar Yu, and Jasmine Lin
 */

public class Movement {

    private int WIDTH;

    private int HEIGHT;

    private Avatar character;

    public Movement(int width, int height, Avatar character) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.character = character;
    }

    /**
     * Attempts to move the character by a specified deltaX and deltaY.
     * Moves only if the target position is within bounds and is a floor tile.
     *
     * @param deltaX The change in X-coordinate
     * @param deltaY The change in Y-coordinate
     */
    public void tryMove(int deltaX, int deltaY) {
        int newX = character.getXPosition() + deltaX;
        int newY = character.getYPosition() + deltaY;

        if (isValidMove(newX, newY)) {
            character.setXPosition(newX);
            character.setYPosition(newY);
        }  // there is a difference between setXPosition and redrawing the avatar there

        if (isLibrary(newX + 1, newY)) {
            // Result button pop up
            System.out.println("Find the titles key words");
        }
    }

    /**
     * Checks whether the move to the specified coordinates is valid.
     * Validates if the target position is within bounds and is a floor tile.
     *
     * @param newX The new X-coordinate
     * @param newY The new Y-coordinate
     * @return True if the move is valid, otherwise False
     */
    boolean isValidMove(int newX, int newY) {
        TETile[][] board = character.getTiles();
        // Check boundaries
        return board[newX][newY] == Tileset.FLOOR || board[newX][newY] == Tileset.PATH;
    }

    boolean isLibrary(int newX, int newY) {
        TETile[][] board = character.getTiles();
        return board[newX][newY] == Tileset.TREE;
    }
}
