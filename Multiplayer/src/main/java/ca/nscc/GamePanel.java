package ca.nscc;

/**
 * Author: W0490409
 * Date: 2024-12-06
 * Filename: Apple.java
 * Description: This file handles everything related to the game actually running.
 */


import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;


public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_HEIGHT = 600;
    static int SCREEN_WIDTH = 600; // Width is not final so the screen can shrink.
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT)/UNIT_SIZE;
    static int DELAY = 100; // Feel free to set this to 300-400 to make it easier to test with two snakes.

    static int running = 1; // Running variable is dependent on which value it has.
    /*
        1: Main Menu
        2: Snake Game Running
        3: Leaderboard
        4: Game Over Screen

        This is how I switch between screens.
     */

    Timer timer;
    static Random random;
    ArrayList<GameObjects> gameObjects; // ArrayList to hold all game objects (apples, poison apples, walls).

    ArrayList<Player> snakes; // ArrayList to hold players.

    static int totalApples = 0; // Total apples that have been eaten by both players.

    static int level; // Current level.
    static String levelString = String.valueOf(level); // Current level as a string so it can go from Level 9 to Level Endless.

    String[] names = {""}; // Array used to hold names in the leaderboard. It was originally size 10 when I was trying to make the top 10 system work.

    int[] highscores = {0}; // Same thing as names, but for the scores.

    Map<Integer, String> topScores = new HashMap<Integer, String>();
    // This was to keep key value pairs of names and scores. I never finished the working top 10 scores,
    // so this isn't needed to make this work, but I kept it in just in case I come back to fix this some
    // day.


    // Background things to initialize game components.
    GamePanel() {
        random = new Random();
        gameObjects = new ArrayList<>(); // This list will track all game objects.
        snakes = new ArrayList<>(); // Make a list for the snakes.
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT)); // Set screen size.
        this.setBackground(Color.red); // Background is red, this will show when the screen starts to shrink.
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        try { // This is where the scores are read from the file. This interaction happens a lot in this file.
            Scanner nameReader = new Scanner(new File("scores.txt")); // Make a scanner to read from the file.
            nameReader.useDelimiter(" "); // Read up until a space.
            for (int i = 0; nameReader.hasNextLine(); i++) {
                String scoreLine = nameReader.next(); // Read every line.
                nameReader.nextLine();
                names[i] = scoreLine; // Store every name read in the array of names. (Should just be one for this version)
            }

            Scanner scoreReader = new Scanner(new File("scores.txt")); // Make another scanner to read the scores.
            for (int i = 0; scoreReader.hasNextLine(); i++) {
                String scoreLine = scoreReader.nextLine(); // Read every line completely.
                if (scoreLine.contains(" ")) { // Check for the space.
                    String score = scoreLine.substring(scoreLine.indexOf(" ") + 1).trim(); // Get rid of everything before and including the space.
                    highscores[i] = Integer.parseInt(score); // Store the score as an integer in the highscores array.
                }
            }
            for (int i = 0; i < names.length; i++) {
                topScores.put(highscores[i], names[i]); // Create key-value pairs of each score from the two arrays.
            }
            // CLose both scanners.
            nameReader.close();
            scoreReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // I get the scores at the start so the leaderboard will be populated.
        startGame(); // Call the start game method.
    }

    public void startGame() {
        // Reset all game values so the game can be restarted.
        gameObjects.clear(); // Do the same thing for game objects.
        level = 1; // Set the level back to 1.
        levelString = "1"; // Do the same for the string version.
        totalApples = 0; // Reset the apples eaten.
        // Add new snakes to the list.
        Player snake1 = new Player(1);
        Player snake2 = new Player(2);
        snakes.add(snake1);
        snakes.add(snake2);

        // Add a new one of each obstacle to the list of obstacles.
        Apple apple = new Apple();
        PoisonApple poisonApple = new PoisonApple();
        Obstacle obstacle = new Obstacle();
        gameObjects.add(obstacle);
        gameObjects.add(poisonApple);
        gameObjects.add(apple);

        // Create new coordinates for each object.
        poisonApple.createNew();
        obstacle.createNew();
        apple.createNew();
        running = 1; // Start on the Main Menu
        timer = new Timer(DELAY, this);
        timer.start();
    }

    // This method is used to display the main menu graphics.
    public void mainMenu(Graphics g) {

        // Draw the green background.
        g.setColor(new Color(0, 54, 1));
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        // Draw the title.
        g.setFont(new Font("Consolas", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.setColor(Color.GREEN);
        g.drawString("snake co-op", (SCREEN_WIDTH - metrics.stringWidth("snake co-op"))/2, SCREEN_HEIGHT/3);

        // Draw the selections. They all have the same length so they line up. I do this for almost every menu.
        g.setFont(new Font("Consolas", Font.PLAIN, 50));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("1 - play", (SCREEN_WIDTH - metrics2.stringWidth("2 - leaderboard"))/2, SCREEN_HEIGHT/2 - UNIT_SIZE);
        g.drawString("2 - leaderboard", (SCREEN_WIDTH - metrics2.stringWidth("2 - leaderboard"))/2, SCREEN_HEIGHT/2 + UNIT_SIZE);
        g.drawString("3 - quit", (SCREEN_WIDTH - metrics2.stringWidth("2 - leaderboard"))/2, SCREEN_HEIGHT/2 + (UNIT_SIZE * 3));

    }

    // Method to draw the leaderboard.
    public void leaderboard(Graphics g) {

        // Draw the background.
        g.setColor(new Color(0, 54, 1));
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        // The title.
        g.setFont(new Font("Consolas", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.setColor(Color.GREEN);
        g.drawString("HIGH SCORES", (SCREEN_WIDTH - metrics.stringWidth("HIGH SCORES"))/2, SCREEN_HEIGHT/8);

        // The selections at the bottom.
        g.setFont(new Font("Consolas", Font.BOLD, 35));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("1 - main menu", (SCREEN_WIDTH - metrics2.stringWidth("X - erase data"))/2, SCREEN_HEIGHT - UNIT_SIZE * 2);
        g.drawString("X - erase data", (SCREEN_WIDTH - metrics2.stringWidth("X - erase data"))/2, SCREEN_HEIGHT - UNIT_SIZE);

//        Arrays.sort(highscores); // This was used when I was trying to track top 10 scores.

        if (names[0].isEmpty()) { // Display a message instead of nothing when there is not an active score.
            g.drawString("NO SCORES", (SCREEN_WIDTH - metrics2.stringWidth("NO SCORES"))/2, SCREEN_HEIGHT/2);
        }
        else {
            for (int i = 0; i < highscores.length; i++) { // For each high score in the array, display the score name and the score itself.
                g.drawString(topScores.get(highscores[i]) + " " + highscores[i], (SCREEN_WIDTH - metrics2.stringWidth("AAA 000"))/2, SCREEN_HEIGHT/2);
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    // Method used to display all things that need to be seen.
    public void draw(Graphics g) {

        // Display the main menu if running says to do so.
        if (running == 1) {
            mainMenu(g);
        }
        // Display the game if running is 2
        else if (running == 2) {
            // Draw the background.
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

            // Draw the lines for the grid.
            for (int i = 0; i < SCREEN_WIDTH / UNIT_SIZE; i++) {
                g.setColor(new Color(0, 105, 35));
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
            }

            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }


            // Draw each game object.
            for (GameObjects object : gameObjects) {
                object.drawObject(g);
            }

            // Draw each snake.
            for (Player player : snakes) {
                player.drawSnake(g);
            }

            // Display the score and level.
            g.setColor(Color.LIGHT_GRAY);
            g.setFont(new Font("Consolas", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Level " + levelString, (SCREEN_WIDTH - metrics.stringWidth("Level " + levelString))/2, UNIT_SIZE + 12);
            g.drawString("Apples Eaten: " + totalApples, (SCREEN_WIDTH - metrics.stringWidth("Apples Eaten: " + totalApples))/2, SCREEN_HEIGHT - 12);

        }
        // Go to the leaderboard if running is 3
        else if (running == 3) {
            leaderboard(g);
        }
        // Running at 4 is Game Over, Man!
        else if (running == 4) {
            snakes.clear(); // Get rid of the snakes right away to prevent any weirdness.
            gameOver(g); // Draw the game over screen.
            if (totalApples != 0 && (totalApples > highscores[0])) { // If the score was better than the current high score and it wasn't zero.

                try { // Write the score to the file.
                    FileWriter myWriter = new FileWriter("scores.txt");
                    myWriter.write("2PS " + totalApples); // No name entry for 2 player.
                    myWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try { // Fetch the new high score.
                    Scanner nameReader = new Scanner(new File("scores.txt"));
                    nameReader.useDelimiter(" ");
                    for (int i = 0; nameReader.hasNextLine(); i++) {
                        String scoreLine = nameReader.next();
                        nameReader.nextLine();
                        names[i] = scoreLine;
                    }

                    Scanner scoreReader = new Scanner(new File("scores.txt"));
                    for (int i = 0; scoreReader.hasNextLine(); i++) {
                        String scoreLine = scoreReader.nextLine();
                        if (scoreLine.contains(" ")) {
                            String score = scoreLine.substring(scoreLine.indexOf(" ") + 1).trim();
                            highscores[i] = Integer.parseInt(score);
                        }
                    }
                    for (int i = 0; i < names.length; i++) {
                        topScores.clear(); // Empty the list of scores and names.
                        topScores.put(highscores[i], names[i]); // Add the new score.
                    }
                    nameReader.close();
                    scoreReader.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    // Method used to display game overs.
    public void gameOver(Graphics g) {

        // Title
        g.setColor(Color.BLACK);
        g.setFont(new Font("Consolas", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("GAME OVER", (SCREEN_WIDTH - metrics.stringWidth("GAME OVER"))/2, SCREEN_HEIGHT/3);

        // Statistics and Options
        g.setFont(new Font("Consolas", Font.BOLD, 50));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Level " + levelString, (SCREEN_WIDTH - metrics2.stringWidth("Level " + levelString))/2, SCREEN_HEIGHT/2 - UNIT_SIZE);
        g.drawString("Apples Eaten: " + totalApples, (SCREEN_WIDTH - metrics2.stringWidth("Apples Eaten: " + totalApples))/2, SCREEN_HEIGHT/2 + UNIT_SIZE);
        g.drawString("1 - Main Menu", (SCREEN_WIDTH - metrics2.stringWidth("1 - Main Menu"))/2, SCREEN_HEIGHT - UNIT_SIZE * 4);
        g.drawString("2 - Quit", (SCREEN_WIDTH - metrics2.stringWidth("1 - Main Menu"))/2, SCREEN_HEIGHT - UNIT_SIZE * 2);

    }

    // Every game tick, do this.
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running == 2) { // As long as the game is going,
            for (Player snake : snakes) {
                snake.move(); // Allow both snakes to move.
                snake.checkCollisions(); // Check if either snake dies.
                if (running == 4) { // Check if the game was set to game over.
                    timer.stop(); // Stop the timer so the wierd speed bug doesn't occur.
                    SCREEN_WIDTH = 600; // Return the screen size to normal.
                }
            }
            for (GameObjects object : gameObjects) {
                object.detectCollision(snakes.getFirst()); // Check for collision with all game objects with both players.
                object.detectCollision(snakes.get(1));
                if (running == 4) { // The same check for a game over.
                    timer.stop();
                    SCREEN_WIDTH = 600;
                }
            }
        }
        repaint();
    }

    // Implementation class used to detect key input.
    public class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) { // Massive switch statement used to see if a player pressed a key and when.
                // SNAKE MOVEMENT FOR ARROW KEYS
                case KeyEvent.VK_LEFT:
                    if (snakes.get(1).getDirection() != 'R' && running == 2) { // Make sure the game is running in addition to checking their current direction.
                        snakes.get(1).setDirection('L');
                    }
                    break;

                case KeyEvent.VK_RIGHT:
                    if (snakes.get(1).getDirection() != 'L' && running == 2) {
                        snakes.get(1).setDirection('R');
                    }
                    break;

                case KeyEvent.VK_UP:
                    if (snakes.get(1).getDirection() != 'D' && running == 2) {
                        snakes.get(1).setDirection('U');
                    }
                    break;

                case KeyEvent.VK_DOWN:
                    if (snakes.get(1).getDirection() != 'U' && running == 2) {
                        snakes.get(1).setDirection('D');
                    }
                    break;

                // WASD MOVEMENT
                case KeyEvent.VK_A:
                    if (snakes.getFirst().getDirection() != 'R' && running == 2) {
                        snakes.getFirst().setDirection('L');
                    }
                    break;

                case KeyEvent.VK_D:
                    if (snakes.getFirst().getDirection() != 'L' && running == 2) {
                        snakes.getFirst().setDirection('R');
                    }
                    break;


                case KeyEvent.VK_W:
                    if (snakes.getFirst().getDirection() != 'D' && running == 2) {
                       snakes.getFirst().setDirection('U');
                    }
                    break;

                case KeyEvent.VK_S:
                    if (snakes.getFirst().getDirection() != 'U' && running == 2) {
                        snakes.getFirst().setDirection('D');
                    }
                    break;


                // PRESSING 1
                case KeyEvent.VK_1:
                    if (running == 1) { // Start game on main menu
                        running = 2;
                    }
                    else if (running == 4) { // Return to main menu on game over screen.
                        startGame();
                    }
                    else if (running == 3) { // Return to main menu from leaderboard.
                        running = 1;
                    }
                    break;
                // PRESSING 2
                case KeyEvent.VK_2:
                    if (running == 1) { // Go to leaderboard from main menu
                        running = 3;
                    }
                    else if (running == 4) { // Quit the game from the game over screen.
                        System.exit(0); // Rage Quit functionality.
                    }
                    break;
                // PRESSING 3
                case KeyEvent.VK_3:
                    if (running == 1) {
                        System.exit(0); // Quit the game from the title screen.
                    }
                    break;
                // PRESSING X
                case KeyEvent.VK_X:
                    if (running == 3) { // If the user is on the leaderboard screen and presses X
                        FileWriter myWriter;
                        try {
                            myWriter = new FileWriter("scores.txt");
                            myWriter.write(""); // Empty the file by writing a blank string.
                            myWriter.close();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        for (int i = 0; i < highscores.length; i++) {
                            highscores[i] = 0; // Reset the arrays.
                            names[i] = "";
                        }
                    }
            }
        }
    }
}
