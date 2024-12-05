package ca.nscc;

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

/*
    Author: Samuel Cook
    File: GamePanel.java
    Date: December 2, 2024
    This file handles everything related to the game actually running.
 */

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_HEIGHT = 600;
    static int DRAW_HEIGHT = 600;
    static int SCREEN_WIDTH = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT)/UNIT_SIZE;
    static int DELAY = 100;
    static final int[] x = new int[GAME_UNITS];
    static final int[] y = new int[GAME_UNITS];
    static int applesEaten;
    static int running = 1;
    Timer timer;
    static Random random;
    ArrayList<GameObjects> gameObjects;
    static int bodyParts;
    char direction;
    static int level;
    static String levelString = String.valueOf(level);

    String[] names = {""};

    int[] highscores = {0};
    Map<Integer, String> topScores = new HashMap<Integer, String>();
    // This was to keep key value pairs of names and scores. I never finished the working top 10 scores,
    // so this isn't needed to make this work, but I kept it in just in case I come back to fix this some
    // day.

    /*
        If score is in range between lowest score and highest score OR greater than highest score
        delete lowest score
        add score
        sort scores
        rewrite the scores in the file with the new sorted list of scores and names

        still need to deal with duplicate scores and duplicate names.
     */

    GamePanel() {
        random = new Random();
        gameObjects = new ArrayList<>(); // This list will track all game objects.
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.red);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        try {
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
                topScores.put(highscores[i], names[i]); // Create key-value pairs of each score in the scores.txt file.
            }
            nameReader.close();
            scoreReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        startGame();
    }

    public void startGame() {
        // Reset all game values so the game can be restarted.
        gameObjects.clear();
        level = 1;
        levelString = "1";
        applesEaten = 0;
        direction = 'D';
        bodyParts = 6;
        for (int i = 1; i < bodyParts; i++) { // The location I picked (-UNIT_SIZE) is meaningless, it's just off the screen.
            x[i] = -UNIT_SIZE;
        }
        x[0] = SCREEN_WIDTH/2;
        y[0] = 0;

        Apple apple = new Apple();
        PoisonApple poisonApple = new PoisonApple();
        Obstacle obstacle = new Obstacle();
        gameObjects.add(obstacle);
        gameObjects.add(poisonApple);
        gameObjects.add(apple);
        poisonApple.createNew();
        obstacle.createNew();
        apple.createNew();
        running = 1;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void mainMenu(Graphics g) {

        g.setColor(new Color(0, 54, 1));
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        g.setFont(new Font("Consolas", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.setColor(Color.GREEN);
        g.drawString("snake", (SCREEN_WIDTH - metrics.stringWidth("snake"))/2, SCREEN_HEIGHT/3);

        g.setFont(new Font("Consolas", Font.PLAIN, 50));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("1 - play", (SCREEN_WIDTH - metrics2.stringWidth("2 - leaderboard"))/2, SCREEN_HEIGHT/2 - UNIT_SIZE);
        g.drawString("2 - leaderboard", (SCREEN_WIDTH - metrics2.stringWidth("2 - leaderboard"))/2, SCREEN_HEIGHT/2 + UNIT_SIZE);
        g.drawString("3 - quit", (SCREEN_WIDTH - metrics2.stringWidth("2 - leaderboard"))/2, SCREEN_HEIGHT/2 + (UNIT_SIZE * 3));

    }

    public void leaderboard(Graphics g) {

        g.setColor(new Color(0, 54, 1));
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        g.setFont(new Font("Consolas", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.setColor(Color.GREEN);
        g.drawString("HIGH SCORES", (SCREEN_WIDTH - metrics.stringWidth("HIGH SCORES"))/2, SCREEN_HEIGHT/8);

        g.setFont(new Font("Consolas", Font.BOLD, 35));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("1 - main menu", (SCREEN_WIDTH - metrics2.stringWidth("X - erase data"))/2, SCREEN_HEIGHT - UNIT_SIZE * 2);
        g.drawString("X - erase data", (SCREEN_WIDTH - metrics2.stringWidth("X - erase data"))/2, SCREEN_HEIGHT - UNIT_SIZE);

//        Arrays.sort(highscores); // This was used when I was trying to track top 10 scores.

        if (names[0].isEmpty()) { // Display a message instead of nothing when there is not an active score.
            g.drawString("NO SCORES", (SCREEN_WIDTH - metrics2.stringWidth("NO SCORES"))/2, SCREEN_HEIGHT/2);
        }
        else {
            for (int i = 0; i < highscores.length; i++) {
                g.drawString(topScores.get(highscores[i]) + " " + highscores[i], (SCREEN_WIDTH - metrics2.stringWidth("AAA 000"))/2, SCREEN_HEIGHT/2);
            }
        }
//g.drawString(topScores.get(highscores[i]) + " " + highscores[i], (SCREEN_WIDTH - metrics2.stringWidth("AAA 000"))/2, SCREEN_HEIGHT - 40 * (i + 3));
// this was the line I used to successfully display 10 scores greatest to least from bottom to top.

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        if (running == 1) {
            mainMenu(g);
        }
        else if (running == 2) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

            for (int i = 0; i < SCREEN_WIDTH / UNIT_SIZE; i++) {
                g.setColor(new Color(0, 105, 35));
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
            }

            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }


            for (GameObjects object : gameObjects) {
                object.drawObject(g);
            }

            // For each player, do this below.
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.LIGHT_GRAY);
            g.setFont(new Font("Consolas", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Level " + levelString, (SCREEN_WIDTH - metrics.stringWidth("Level " + levelString))/2, UNIT_SIZE + 12);
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, SCREEN_HEIGHT - 12);

        }
        else if (running == 3) {
            leaderboard(g);
        }
        else if (running == 4) {
            gameOver(g);
            if (applesEaten != 0 && (applesEaten > highscores[0])) {

                String name;
                do {
                    name = JOptionPane.showInputDialog(
                            null,
                            "Enter name (3 letters): ",
                            "New High Score!",
                            JOptionPane.PLAIN_MESSAGE
                    );
                    if (name == null) { // Catches the user closing the dialogue box.
                        name = "???";
                    }
                    name = name.trim().toUpperCase(); // Trim the user's entry and make it capitalized.
                } while (name.length() != 3);

                // Cover up the remnants of the window.
                g.setColor(Color.red);
                g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
                gameOver(g);

                try {
                    FileWriter myWriter = new FileWriter("scores.txt");
                    myWriter.write(name + " " + applesEaten);
                    myWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
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
                        topScores.put(highscores[i], names[i]); // refresh the list of scores.
                    }
                    nameReader.close();
                    scoreReader.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch(direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
        }
    }


    public void checkCollisions() {
        // Checks if the head touches the body.
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = 4;
            }
        }

        // Check if head touches the left border.
        if (x[0] < 0) {
            running = 4;
        }

        // Check for right.
        if (x[0] > SCREEN_WIDTH - UNIT_SIZE) {
            running = 4;
        }

        // Check for bottom.
        if (y[0] < 0) {
            running = 4;
        }

        // Check for top.
        if (y[0] > SCREEN_HEIGHT - UNIT_SIZE) {
            running = 4;
        }

        if (running == 4) {
            timer.stop();
            SCREEN_WIDTH = 600;
        }

    }

    public void gameOver(Graphics g) {
        // Game Over screen.
        g.setColor(Color.BLACK);
        g.setFont(new Font("Consolas", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("GAME OVER", (SCREEN_WIDTH - metrics.stringWidth("GAME OVER"))/2, SCREEN_HEIGHT/3);

        g.setFont(new Font("Consolas", Font.BOLD, 50));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Level " + levelString, (SCREEN_WIDTH - metrics2.stringWidth("Level " + levelString))/2, SCREEN_HEIGHT/2 - UNIT_SIZE);
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score: " + applesEaten))/2, SCREEN_HEIGHT/2 + UNIT_SIZE);
        g.drawString("1 - Main Menu", (SCREEN_WIDTH - metrics2.stringWidth("1 - Main Menu"))/2, SCREEN_HEIGHT - UNIT_SIZE * 4);
        g.drawString("2 - Quit", (SCREEN_WIDTH - metrics2.stringWidth("1 - Main Menu"))/2, SCREEN_HEIGHT - UNIT_SIZE * 2);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running == 2) {
            move();
            for (GameObjects object : gameObjects) {
                object.detectCollision();
            }
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_1:
                    if (running == 1) {
                        running = 2;
                    }
                    else if (running == 4) {
                        startGame();
                    }
                    else if (running == 3) {
                        running = 1;
                    }
                    break;
                case KeyEvent.VK_2:
                    if (running == 1) {
                        running = 3;
                    }
                    else if (running == 4) {
                        System.exit(0);
                    }
                    break;
                case KeyEvent.VK_3:
                    if (running == 1) {
                        System.exit(0);
                    }
                    break;
                case KeyEvent.VK_X: // Used to erase stored scores.
                    if (running == 3) {
                        FileWriter myWriter;
                        try {
                            myWriter = new FileWriter("scores.txt");
                            myWriter.write(""); // Empty the file.
                            myWriter.close();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        for (int i = 0; i < highscores.length; i++) {
                            highscores[i] = 0;
                            names[i] = "";
                        }
                    }
            }
        }
    }
}
