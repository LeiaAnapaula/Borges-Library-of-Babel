package core;

import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;

public class homeScreen {
    public static StringBuilder seedInput = new StringBuilder();
    public static StringBuilder playerName = new StringBuilder();
    public static StringBuilder userInput = new StringBuilder();
    private static boolean isNewGameInitiated = false;
    private static boolean isNameInputScreen = false;
    public static void main(String[] args) {
        StdDraw.setCanvasSize(800, 600);
        StdDraw.setXscale(0, 1);
        StdDraw.setYscale(0, 1);

        StdDraw.enableDoubleBuffering();

        while (true) {
            StdDraw.clear(StdDraw.BLACK);
            StdDraw.setPenColor(StdDraw.WHITE);
            Font font1 = new Font("SansSerif", Font.BOLD, 40);
            StdDraw.setFont(font1);
            StdDraw.text(0.5, 0.8, "The Library of Jorge Luis Borges");
            Font font2 = new Font("SansSerif", Font.PLAIN, 30);
            StdDraw.setFont(font2);

            if (isNameInputScreen) {
                // Display name input screen
                StdDraw.text(0.5, 0.50, "What is your name?");
                StdDraw.text(0.5, 0.45, playerName.toString());  // Display entered name
            } else if (isNewGameInitiated) {
                // Display "Enter Seed" text when 'N' is pressed
                StdDraw.text(0.5, 0.50, "Enter Seed: " + seedInput.toString());
                StdDraw.text(0.5, 0.45, "Press 'S' to finalize the seed");
            } else {
                // Display default options
                StdDraw.text(0.5, 0.6, "New Game (N)");
                StdDraw.text(0.5, 0.55, "Load Game (L)");
                StdDraw.text(0.5, 0.50, "Quit (Q)");
            }

            StdDraw.show();
            StdDraw.pause(20);

            StdDraw.clear();

            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                userInput.append(key);

                if (isNameInputScreen) {
                    if (key != '\n') {
                        if (key == 8 && !playerName.isEmpty()) {  // Backspace key
                            playerName.deleteCharAt(playerName.length() - 1);
                        } else {
                            playerName.append(key);
                        }
                    } else {
                        // Move to the next screen or perform actions with the entered name
                        isNameInputScreen = false;
                        StdDraw.clear(Color.black);
                        Main.generateWorld(getSeed(), getPlayerName(), userInput);
                    }
                } else {
                    if (key == 'N' || key == 'n') {
                        isNewGameInitiated = true;
                        seedInput.setLength(0);
                    } else if (key == 'Q' || key == 'q') {
                        System.exit(0);
                    } else if (Character.isDigit(key)) {
                        seedInput.append(key);
                    } else if (key == 'S' || key == 's') {
                        if (!seedInput.isEmpty()) {
                            // Ask for the player's name after finalizing the seed
                            isNameInputScreen = true;
                        }
                    } else if (key == 8) { // Backspace key
                        if (!seedInput.isEmpty()) {
                            seedInput.deleteCharAt(seedInput.length() - 1);
                        }
                    }
                }
            }
        }
    }

    public static String getSeed() {
        return seedInput.toString();
    }

    public static String getPlayerName() {
        return playerName.toString();
    }
}
