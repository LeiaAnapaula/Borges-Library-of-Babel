package core;

import edu.princeton.cs.algs4.StdDraw;
import java.awt.Font;

public class resultScreen {
    private static StringBuilder result = new StringBuilder();
    private static boolean isResultInputScreen = false;
    String correctAnswer;

    public static void main(String[] args) {
        String correctAnswer = args.length > 0 ? args[0] : "YourAnswer";  // Default answer if not provided

        StdDraw.setCanvasSize(800, 600);
        StdDraw.setXscale(0, 1);
        StdDraw.setYscale(0, 1);

        StdDraw.enableDoubleBuffering();

        while (true) {
            StdDraw.clear(StdDraw.BLACK);
            StdDraw.setPenColor(StdDraw.WHITE);
            Font font1 = new Font("SansSerif", Font.BOLD, 40);
            StdDraw.setFont(font1);
            StdDraw.text(0.5, 0.8, "Result Screen");

            Font font2 = new Font("SansSerif", Font.PLAIN, 30);
            StdDraw.setFont(font2);

            if (isResultInputScreen) {
                // Display result input screen
                StdDraw.text(0.5, 0.50, "Enter Result:");
                StdDraw.text(0.5, 0.45, result.toString());  // Display entered result
            } else {
                // Display default options
                StdDraw.text(0.5, 0.6, "Press Enter to Submit Result");
                StdDraw.text(0.5, 0.55, "Quit (Q)");
            }

            StdDraw.show();
            StdDraw.pause(20);

            StdDraw.clear();

            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();

                if (isResultInputScreen) {
                    if (key != '\n') {
                        if (key == 8 && result.length() > 0) {  // Backspace key
                            result.deleteCharAt(result.length() - 1);
                        } else {
                            result.append(key);
                        }
                    } else {
                        // Check result and perform actions accordingly
                        checkResult(correctAnswer);
                        result.setLength(0); // Clear the result for the next input
                    }
                } else {
                    if (key == '\n') {
                        isResultInputScreen = true;
                    } else if (key == 'Q' || key == 'q') {
                        System.exit(0);
                    }
                }
            }
        }
    }

    private static void checkResult(String correctAnswer) {
        String answer = correctAnswer;
        if (result.toString().equalsIgnoreCase(answer)) {
            // Clear all text and display winning message
            StdDraw.clear(StdDraw.BLACK);
            StdDraw.text(0.5, 0.5, "You win!");
            StdDraw.show();
            StdDraw.pause(2000);  // Display for 2 seconds
            System.exit(0);
        } else {
            // Display message and return to the previous screen
            StdDraw.clear(StdDraw.BLACK);
            StdDraw.text(0.5, 0.5, "That is not the book. You have lost.");
            StdDraw.show();
            StdDraw.pause(2000);  // Display for 2 seconds
            System.exit(0);
        }
    }
}