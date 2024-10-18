package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TETile;
import tileengine.Tileset;
import tileengine.TERenderer;

import java.awt.*;
import java.util.*;
import java.util.List;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

public class World {
    private long SEED;
    private TETile[][] tiles;
    private boolean[][] grid;
    private ArrayDeque<String> books;
    private ArrayDeque<Room> rooms;
    private final int WIDTH, HEIGHT, minRoomDimension, maxRoomDimension;
    private static final int MIN_ROOM_DISTANCE = 2; // Minimum distance between rooms
    private TETile blankTile = Tileset.NOTHING;
    private final TETile floorTile = Tileset.FLOOR;
    private final TETile wallTile = Tileset.FLOWER;
    private final TETile bookTile = Tileset.TREE;
    private final Random RANDOM;
    private List<String> bookTitles;

    private String correctBook;
    private String currentBook;

    public StringBuilder userInput;

    // INTERACTIVITY

    Avatar character;
    Villain villain;
    String playerName;

    private int characterX;
    private int characterY;
    private final TERenderer ter = new TERenderer();

    public World(int width, int height, long seed, String playerName, StringBuilder userInput) {
        StdDraw.setXscale(0, 1);
        StdDraw.setYscale(0, 1);

        // Enable double buffering
        StdDraw.enableDoubleBuffering();

        this.WIDTH = width;
        this.HEIGHT = height;
        tiles = new TETile[WIDTH][HEIGHT];
        this.playerName = playerName;

        grid = new boolean[WIDTH][HEIGHT];
        minRoomDimension = 6;
        maxRoomDimension = 20;
        SEED = seed;
        RANDOM = new Random(SEED);
        rooms = new ArrayDeque<>();
        //currentBook = getCurrentBook();
        //this.userInput.append(userInput);

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                tiles[x][y] = blankTile; // Initializes empty grid
            }
        }

        for (int row = 0; row < HEIGHT; row++) { // Iterates through each row.
            fillRow(row);
        }

        // connect rooms
        Hallways allHallways = new Hallways(rooms, tiles, WIDTH, HEIGHT, grid);
        allHallways.createHallway(rooms);
        // why is it drawing the book's shelves but not the hallways. Adjacency is buggy.

        //loadBookTitles("proj3/src/core/bookTitles");

        Room startRoom = getRandomRoom();
        Point characterPosition = getValidCharacterPosition(startRoom);
        characterX = characterPosition.x;
        characterY = characterPosition.y;

        // Interactivity
        character = new Avatar(characterX, characterY, tiles, WIDTH, HEIGHT);

        // Villain initialization
        Room villainRoom = getAdjacentRoom(); // Pass the starting room for adjacency check
        Point villainPosition = getValidVillainPosition(villainRoom);
        int villainX = villainPosition.x;
        int villainY = villainPosition.y;

        villain = new Villain(villainX, villainY, tiles, WIDTH, HEIGHT, character);

//        for (Room room : rooms) {
//            room.fillBookshelf(bookTile);
//            room.fillBook(pickRandomBook(RANDOM)); // randomly assigns a book from the list to each room
//        }
        villain.drawVillain();


        //correctBook = pickRightBook(RANDOM); // assigns one right book to the world

//        drawHUD();
//
//        StdDraw.show();
    }

    public TETile[][] getTiles() {
        return tiles;
    }
    /*
    This method has a 25% chance of generating a new shape for each tile in a row.
    Note that this number was arbitrarily chosen.
     */
    private void fillRow(int row) {
        for (int tile = 0; tile < WIDTH; tile++) {
            // Check if the tile is in an empty space and randomly decide whether to generate a shape
            if (emptySpace(tile, row) && RANDOM.nextDouble() < 0.25) {
                int width = getRandomDimension(tile, WIDTH);
                int height = getRandomDimension(row, HEIGHT);
                if (checkDimensions(row, tile, width, height) && checkNeighbors(row, tile, width, height)) {
                    // Checks that there are no conflicting tiles in the way first!
                    fillShape(row, tile, width, height);
                }
            }
        }
    }
    private int getRandomDimension(int tile, int dimension) {
        int bound = dimension - tile - 1;
        if (bound == 0) {
            return 0;
        } else {
            return RANDOM.nextInt(bound);
        }
    }
    private boolean checkDimensions(int row, int tile, int width, int height) {
        boolean minCheck = width >= minRoomDimension && height >= minRoomDimension;
        boolean maxCheck = width <= maxRoomDimension && height <= maxRoomDimension;
        if (minCheck && maxCheck) {
            return row + height < HEIGHT && tile + width < WIDTH;
        }
        return false;
    }
    private boolean checkNeighbors(int row, int tile, int width, int height) {
        for (int x = tile - MIN_ROOM_DISTANCE; x < tile + width + MIN_ROOM_DISTANCE; x++) {
            for (int y = row - MIN_ROOM_DISTANCE; y < row + height + MIN_ROOM_DISTANCE; y++) {
                if (validTile(x, y) && !emptySpace(x, y)) {
                    // There is a shape at said coordinate!
                    return false; // Conflict with an existing shape
                }
            }
        }
        return true; // No conflict
    }
    private boolean validTile(int x, int y) {
        return x >= 0 && y >= 0 && x < WIDTH && y < HEIGHT;
    }
    /*
    @usage This method fills in a random shape and updates the grid to ensure that shapes
           do not generate over each other.
    @param row The y-coordinate of the left-hand corner of the newly generated shape.
    @param tile The x-coordinate of the left-hand corner of the newly generated shape.
    @param width The width of the newly generated shape.
    @param height The height of the newly generated shape.
     */
    private void fillShape(int row, int tile, int width, int height) {
        Room generatedRoom;
        updateGrid(row, tile, width, height);
        if (tile == width) {
            generatedRoom = new CircleRoom(row, tile, width, height, tiles);
        } else {
            int randomShape = RANDOM.nextInt(3);
            if (randomShape == 0) {
                generatedRoom = new RectangleRoom(row, tile, width, height, tiles);
            } else if (randomShape == 1) {
                generatedRoom = new HexagonRoom(row, tile, width, height, tiles);
            } else {
                generatedRoom = new OvalRoom(row, tile, width, height, tiles);
            }
        }
        generatedRoom.fillWorld(tiles, blankTile, wallTile, floorTile, bookTile);
        rooms.add(generatedRoom);
    }
    /*
    @usage Ensures that a shape does not exist at point (tile, row) in the grid.
    Returns true if there is an empty space at said point in the grid.
     */
    public boolean emptySpace(int tile, int row) {
        return !grid[tile][row];
    }
    /*
    @usage Updates the grid to show spaces occupied by the shape to be generated.
     */
    private void updateGrid(int row, int tile, int width, int height) {
        for (int x = tile; x < tile + width; x++) {
            for (int y = row; y < row + height; y++) {
                // assign the value of all the tiles after given tile- to be true
                grid[x][y] = true;
            }
        }
    }

    // INTERACTIVITY STARTS HERE -----------------------------------------------------------

        // 1. Position Avatar and Villain
    private Point getValidCharacterPosition(Room room) {
        int startX = room.tile;
        int startY = room.row + room.height - 2;

        for (int x = startX; x < startX + room.width; x++) {
            for (int y = startY; y > room.row; y--) {
                if (isFloorTile(x, y)) {
                    return new Point(x, y);
                }
            }
        }
        return new Point(startX, startY); // Return room's initial X position if no floor tile is found
    }
    private boolean isFloorTile(int x, int y) {
        return tiles[x][y] == Tileset.FLOOR;
    }
    private Point getValidVillainPosition(Room room) {
        int startX = room.tile;

        for (int x = startX; x < startX + room.width; x++) {
            int villainY = room.row + room.height - 2;
            if (isFloorTile(x, villainY)) {
                return new Point(x, villainY);
            }
        }
        return new Point(startX + 1, room.row + room.height - 2); // Default position if no valid tile found
    }
    public Room getRandomRoom() {
        if (rooms.isEmpty()) {
            return null; // If no connected rooms, return null or handle the case accordingly
        } else {
            List<Room> shuffledRooms = new ArrayList<>(rooms);
            Collections.shuffle(shuffledRooms); // Shuffle the list

            Random random = new Random();
            int randomIndex = random.nextInt(shuffledRooms.size());
            return shuffledRooms.get(randomIndex);
        }
    }
    private Room getAdjacentRoom() {
        Room randomRoom = getRandomRoom();
        int randomDirection = RANDOM.nextInt(4);

        int randomX = randomRoom.tile;
        int randomY = randomRoom.row;

        if (randomDirection == 0 && randomY + randomRoom.height < HEIGHT - 1) {
            // Up
            randomY += randomRoom.height;
        } else if (randomDirection == 1 && randomY - randomRoom.height > 0) {
            // Down
            randomY -= randomRoom.height;
        } else if (randomDirection == 2 && randomX - randomRoom.width > 0) {
            // Left
            randomX -= randomRoom.width;
        } else if (randomDirection == 3 && randomX + randomRoom.width < WIDTH - 1) {
            // Right
            randomX += randomRoom.width;
        }

        return getRoom(randomX, randomY);
    }
    private Room getRoom(int randomX, int randomY) {
        for (Room room : rooms) {
            if (room.tile == randomX && room.row == randomY) {
                return room;
            }
            return room;
        }
        return null;
    }

    public void runGame() {
        while (!isGameOver()) {
            renderBoard();
            updateBoard();
            StdDraw.pause(100); // Adjust the pause duration as needed
        }
    }

    void updateBoard() {
        Avatar you = character;
        boolean avatarMoved = false;

        if (StdDraw.hasNextKeyTyped()) {
            char letter = StdDraw.nextKeyTyped();
            //userInput.append(letter); // for now commenting all userInput strings
            avatarMoved = true;
            if (letter == 'a' || letter == 'A') {
                character.moveLeft();

            } else if (letter == 'w' || letter == 'W') {
                character.moveUp();

            } else if (letter == 'd' || letter == 'D') {
                character.moveRight();

            } else if (letter == 's' || letter == 'S') {
                character.moveDown();

            } else if (letter == 'q' || letter == 'Q') {
                System.exit(0);
            } else if (letter == ':') {
                if (StdDraw.hasNextKeyTyped()) {
                    char letter2 = StdDraw.nextKeyTyped();
                    userInput.append(letter2);
                    if (letter2 == 'q' || letter2 == 'Q') {
                        String saveFile = "saveFile.txt";
                        byte[] bytes = userInput.toString().getBytes();
                        try {
                            Files.write(Path.of(saveFile), bytes);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
//            else if (letter == 'r') {
//                resultScreen.main(new String[]{correctBook});
//            }
            Avatar.draw(you, tiles, you.x, you.y);

        }

        drawHUD(); // Add this line to render the HUD after updating the board

        if (avatarMoved) {
            villain.findPathToAvatar();
            villain.drawPath();
            villain.moveVillain();
        }
    }

    private boolean isGameOver() {
        int avatarX = character.getXPosition();
        int avatarY = character.getYPosition();

        int villainX = villain.getXPosition();
        int villainY = villain.getYPosition();

        // Game over if the villain is adjacent to the player in any direction
        boolean villainCatchesPlayer = Math.abs(avatarX - villainX) <= 1 && Math.abs(avatarY - villainY) <= 1;

        boolean wrongListOfWords = false; // Replace this with your actual logic

        if (villainCatchesPlayer) {
            StdDraw.clear(StdDraw.BLACK);
            StdDraw.text(0.5, 0.5, "Game over!");
            StdDraw.show();
            StdDraw.pause(2000);  // Display for 2 seconds
            System.exit(0);
        }

        // The game is over if condition is true
        return villainCatchesPlayer;

    }

    private void drawHUD() {
        // Display HUD elements
        double x = 0.8;
        double y = 0.8;

        StdDraw.setFont(new Font("Monaco", Font.BOLD, 12));
        StdDraw.setPenColor(Color.WHITE);

        // Display HUD elements without using WIDTH and HEIGHT
        StdDraw.text(x, y, "Current Tile: " + tiles[characterX][characterY].description());
        StdDraw.text(x, y, "Current Book: " + currentBook);
        StdDraw.text(x, y - 0.05, "Result (R)");
        StdDraw.text(x, y - 0.10, "Load (L)");
        StdDraw.setPenColor(Color.GREEN);
        StdDraw.text(x, 0.15, "Quit (Q)");
        StdDraw.text(x, y - 0.20, "Quit & Save (:Q)");

        // Draw player and villain symbols
        StdDraw.text(x, y - 0.30, "@ - " + playerName);
        StdDraw.setPenColor(Color.RED);
        StdDraw.text(x, y - 0.35, "V - Villain");
        StdDraw.setPenColor(Color.BLACK);

        StdDraw.text(x, y - 0.4, "Mouse Cursor");
        StdDraw.show();
    }
    private void renderBoard() {
        ter.renderFrame(tiles);
    }

    private void loadBookTitles(String filePath) {
        try {
            Path path = Paths.get(filePath);
            bookTitles = Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private String pickRandomBook(Random random) {
//        int randomIndex = random.nextInt(bookTitles.size());
//
//        // Retrieve the random book title
//        String randomBook = bookTitles.get(randomIndex);
//        books.add(randomBook);
//        return randomBook;
//    }
//
//    private String pickRightBook(Random random) {
//        int randomIndex = random.nextInt(books.size());
//
//        // Convert ArrayDeque to an array
//        String[] bookArray = books.toArray(new String[0]);
//
//        // Return the book at the random index
//        return bookArray[randomIndex];
//    }
//
//    private String getCurrentBook() {
//        Room currentRoom = getRoom(characterX, characterY);
//        assert currentRoom != null;
//        return currentRoom.getBook();
//    }
}


