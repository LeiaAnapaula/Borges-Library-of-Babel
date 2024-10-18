package core;

import tileengine.TERenderer;

public class Main {
    public static void generateWorld(String stringSeed, String playerName, StringBuilder userInput) {
        int libraryWidth = 50; // 10 will be added on for the HUD implementation
        int libraryHeight = 30;
        long seed = Long.parseLong(stringSeed);

        World borgesLibrary = new World(libraryWidth, libraryHeight, seed, playerName, userInput);

        // Render the Library Tiles
        TERenderer libraryRenderer = new TERenderer();
        libraryRenderer.initialize(libraryWidth + 10, libraryHeight);
        libraryRenderer.drawTiles(borgesLibrary.getTiles());

        // Show the updated canvas
        libraryRenderer.renderFrame(borgesLibrary.getTiles());
        borgesLibrary.runGame();
    }

    public static void main(String[] args) {
        homeScreen.main(args);  // Run the homeScreen
    }
}