//package core;
//
//import edu.princeton.cs.algs4.StdDraw;
//import tileengine.TETile;
//import tileengine.TERenderer;
//import tileengine.Tileset;
//
//import java.util.*;
//
///**
// *  Provides the logic for Tetris.
// *
// *  @author Erik Nelson, Omar Yu, Noah Adhikari, Jasmine Lin
// */
//
//public class Tetris {
//
//    private static int WIDTH = 10;
//    private static int HEIGHT = 20;
//
//    // Tetrominoes spawn above the area we display, so we'll have our Tetris board have a
//    // greater height than what is displayed.
//    private static int GAME_HEIGHT = 25;
//
//    // Contains the tiles for the board.
//    private TETile[][] board;
//
//    // Helps handle movement of pieces.
//    private Movement movement;
//
//    // Checks for if the game is over.
//    private boolean isGameOver;
//
//    // The current game's score.
//    private int score;
//
//    /**
//     * Checks for if the game is over based on the isGameOver parameter.
//     * @return boolean representing whether the game is over or not
//     */
//    private boolean isGameOver() {
//        return isGameOver;
//    }
//
//    /**
//     * Renders the game board and score to the screen.
//     */
//    private void renderBoard() {
//        // renderFrame
//        ter.renderFrame(board);
//        // renderScore
//        renderScore();
//        if (auxFilled) {
//            auxToBoard();
//        } else {
//            fillBoard(Tileset.NOTHING);
//        }
//    }
//
//    /**
//     * Creates a new Tetromino and updates the instance variable
//     * accordingly. Flags the game to end if the top of the board
//     * is filled and the new piece cannot be spawned.
//     */
//    private void spawnPiece() {
//        // The game ends if this tile is filled
//        if (board[4][19] != Tileset.NOTHING) {
//            isGameOver = true;
//        }
//
//        // Otherwise, spawn a new piece and set its position to the spawn point
//        currentTetromino = Tetromino.values()[bagRandom.getValue()];
//        currentTetromino.reset();
//    }
//
//
//    /**
//     * Determines if a new frame should be rendered.
//     * This estimates a 60 fps cap on the rendered window.
//     */
//    public boolean shouldRenderNewFrame() {
//        if (frameDeltaTime() > 16) {
//            resetFrameTimer();
//            return true;
//        }
//        return false;
//    }
//
//    /**
//     * Updates the board based on the user input. Makes the appropriate moves
//     * depending on the user's input.
//     */
//    private void updateBoard() {
//        // Grabs the current piece.
//        Tetromino t = currentTetromino;
//        if (actionDeltaTime() > 1000) {
//            movement.dropDown();
//            resetActionTimer();
//            Tetromino.draw(t, board, t.pos.x, t.pos.y);
//            return;
//        }
//
//        // TODO: Implement interactivity, so the user is able to input the keystrokes to move
//        //  the tile and rotate the tile. You'll want to use some provided helper methods here.
//        if (StdDraw.hasNextKeyTyped()) {
//            // if k is a, s, d, q or w
//            char letter = StdDraw.nextKeyTyped();
//
//            if (letter == 'a') {
//                movement.tryMove(- 1, 0);
//            }
//
//            if (letter == 's') {
//                movement.tryMove(0, -1);
//            }
//
//            if (letter == 'd') {
//                movement.tryMove(1, 0);
//            }
//
//            if (letter == 'q') {
//                movement.rotateRight();
//            }
//
//            if (letter == 'w') {
//                movement.rotateLeft();
//            }
//
//        }
//
//
//        Tetromino.draw(t, board, t.pos.x, t.pos.y);
//    }
//
//    /**
//     * Increments the score based on the number of lines that are cleared.
//     *
//     * @param linesCleared
//     */
//    private void incrementScore(int linesCleared) {
//        // TODO: Increment the score based on the number of lines cleared.
//        if (linesCleared == 1) {
//            score += 100;
//        }
//        else if (linesCleared == 2) {
//            score += 300;
//        }
//        else if (linesCleared == 3) {
//            score += 500;
//        }
//        else if (linesCleared == 4) {
//            score += 800;
//        }
//
//
//    }
//
//    /**
//     * Clears lines/rows on the board that are horizontally filled.
//     * Repeats this process for cascading effects and updates score accordingly.
//     */
//
//    private void clearLines() {
//        // Keeps track of the current number lines cleared
//        int linesCleared = 0;
//
//        List<Integer> rowsToClear = new ArrayList<>();
//
//        // Check how many lines have been completed and add them to rowsToClear list.
//        for (int r = 0; r < HEIGHT; r++) {
//            boolean rowCompleted = true;
//            for (int c = 0; c < WIDTH; c++) {
//                if (board[c][r] == Tileset.NOTHING) {
//                    rowCompleted = false;
//                    break;
//                }
//            }
//            if (rowCompleted) {
//                rowsToClear.add(r);
//                linesCleared++;
//            }
//        }
//
//        // Clear completed rows and shift upper rows down
//        for (int rowToClear : rowsToClear) {
//            for (int r = rowToClear; r < HEIGHT - 1; r++) {
//                for (int c = 0; c < WIDTH; c++) {
//                    board[c][r] = board[c][r + 1]; // Shift the upper rows down
//                }
//            }
//            for (int c = 0; c < WIDTH; c++) {
//                board[c][HEIGHT - 1] = Tileset.NOTHING; // Clear the top row
//            }
//        }
//
//        // Increment the score based on the number of lines cleared.
//        incrementScore(linesCleared);
//
//        fillAux();
//        renderBoard();
//    }
//
//    /**
//     * Where the game logic takes place. The game should continue as long as the game isn't
//     * over.
//     */
//    public void runGame() {
//        resetActionTimer();
//        resetFrameTimer();
//
//        // TODO: First spawn a piece
//        spawnPiece();
//
//        // TODO: Set up your game loop. The game should keep running until the game is over.
//        // Use helper methods inside your game loop, according to the spec description.
//        while (!isGameOver()) {
//            if (shouldRenderNewFrame()) {
//                if (currentTetromino == null) {
//                    clearLines();
//                    spawnPiece();
//                }
//                updateBoard();
//                renderBoard();
//            }
//        }
//    }
//
//    /**
//     * Renders the score using the StdDraw library.
//     */
//    private void renderScore() {
//        // TODO: Use the StdDraw library to draw out the score.
//        StdDraw.setPenColor(255, 255, 255);
//        StdDraw.text(7, 19, "Score: " + score);
//        StdDraw.show();
//    }
//
//    /**
//     * Use this method to run Tetris.
//     * @param args
//     */
//    public static void main(String[] args) {
//        long seed = args.length > 0 ? Long.parseLong(args[0]) : (new Random()).nextLong();
//        Tetris tetris = new Tetris(seed);
//        tetris.runGame();
//    }
//
//    /**
//     * Everything below here you don't need to touch.
//     */
//
//    // This is our tile rendering engine.
//    private final TERenderer ter = new TERenderer();
//
//    // Used for randomizing which pieces are spawned.
//    private Random random;
//    private tetris.BagRandomizer bagRandom;
//
//    private long prevActionTimestamp;
//    private long prevFrameTimestamp;
//
//    // The auxiliary board. At each time step, as the piece moves down, the board
//    // is cleared and redrawn, so we keep an auxiliary board to track what has been
//    // placed so far to help render the current game board as it updates.
//    private TETile[][] auxiliary;
//    private boolean auxFilled;
//
//    public Tetris(long seed) {
//        board = new TETile[WIDTH][GAME_HEIGHT];
//        auxiliary = new TETile[WIDTH][GAME_HEIGHT];
//        random = new Random(seed);
//        bagRandom = new BagRandomizer(random, Tetromino.values().length);
//        auxFilled = false;
//        movement = new Movement(WIDTH, GAME_HEIGHT, this);
//
//        ter.initialize(WIDTH, HEIGHT);
//        fillBoard(Tileset.NOTHING);
//        fillAux();
//    }
//
//    // Setter and getter methods.
//
//    /**
//     * Returns the current game board.
//     * @return
//     */
//    public TETile[][] getBoard() {
//        return board;
//    }
//
//    /**
//     * Returns the current auxiliary board.
//     * @return
//     */
//    public TETile[][] getAuxiliary() {
//        return auxiliary;
//    }
//
//
//    /**
//     * Returns the current Tetromino/piece.
//     * @return
//     */
//    public Tetromino getCurrentTetromino() {
//        return currentTetromino;
//    }
//
//    /**
//     * Sets the current Tetromino to null.
//     * @return
//     */
//    public void setCurrentTetromino() {
//        currentTetromino = null;
//    }
//
//    /**
//     * Sets the boolean auxFilled to true;
//     */
//    public void setAuxTrue() {
//        auxFilled = true;
//    }
//
//    /**
//     * Fills the entire board with the specific tile that is passed in.
//     * @param tile
//     */
//    private void fillBoard(TETile tile) {
//        for (int i = 0; i < board.length; i++) {
//            for (int j = 0; j < board[0].length; j++) {
//                board[i][j] = tile;
//            }
//        }
//    }
//
//    /**
//     * Copies the contents of the src array into the dest array using
//     * System.arraycopy.
//     * @param src
//     * @param dest
//     */
//    private static void copyArray(TETile[][] src, TETile[][] dest) {
//        for (int i = 0; i < src.length; i++) {
//            System.arraycopy(src[i], 0, dest[i], 0, src[0].length);
//        }
//    }
//
//    /**
//     * Copies over the tiles from the game board to the auxiliary board.
//     */
//    public void fillAux() {
//        copyArray(board, auxiliary);
//    }
//
//    /**
//     * Copies over the tiles from the auxiliary board to the game board.
//     */
//    private void auxToBoard() {
//        copyArray(auxiliary, board);
//    }
//
//    /**
//     * Calculates the delta time with the previous action.
//     * @return the amount of time between the previous Tetromino movement with the present
//     */
//    private long actionDeltaTime() {
//        return System.currentTimeMillis() - prevActionTimestamp;
//    }
//
//    /**
//     * Calculates the delta time with the previous frame.
//     * @return the amount of time between the previous frame with the present
//     */
//    private long frameDeltaTime() {
//        return System.currentTimeMillis() - prevFrameTimestamp;
//    }
//
//    /**
//     * Resets the action timestamp to the current time in milliseconds.
//     */
//    private void resetActionTimer() {
//        prevActionTimestamp = System.currentTimeMillis();
//    }
//
//    /**
//     * Resets the frame timestamp to the current time in milliseconds.
//     */
//    private void resetFrameTimer() {
//        prevFrameTimestamp = System.currentTimeMillis();
//    }
//
//}
