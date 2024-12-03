package ca.nscc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

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
    static boolean running = false;
    Timer timer;
    static Random random;
    ArrayList<GameObjects> gameObjects;
    static int bodyParts = 6;
    char direction = 'R';
    static int level = 1;
    static String levelString = String.valueOf(level);



    GamePanel() {
        random = new Random();
        gameObjects = new ArrayList<>(); // This list will track all game objects.
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.red);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        Apple apple = new Apple();
        PoisonApple poisonApple = new PoisonApple();
        Obstacle obstacle = new Obstacle();
        gameObjects.add(obstacle);
        gameObjects.add(poisonApple);
        gameObjects.add(apple);
        poisonApple.createNew();
        obstacle.createNew();
        apple.createNew();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
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
        else {
            gameOver(g);
            // WRITE SCORE TO FILE HERE ////////////////////////////////////////////////
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
                running = false;
            }
        }

        // Check if head touches the left border.
        if (x[0] < 0) {
            running = false;
        }

        // Check for right.
        if (x[0] > SCREEN_WIDTH - UNIT_SIZE) {
            running = false;
        }

        // Check for bottom.
        if (y[0] < 0) {
            running = false;
        }

        // Check for top.
        if (y[0] > SCREEN_HEIGHT - UNIT_SIZE) {
            running = false;
        }

        if (!running) {
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

        g.setColor(Color.BLACK);
        g.setFont(new Font("Consolas", Font.BOLD, 50));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Level " + levelString, (SCREEN_WIDTH - metrics2.stringWidth("Level " + levelString))/2, SCREEN_HEIGHT/2 - UNIT_SIZE);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Consolas", Font.BOLD, 50));
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics3.stringWidth("Score: " + applesEaten))/2, SCREEN_HEIGHT/2 + UNIT_SIZE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
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
                case KeyEvent.VK_W:
                    running = false;
            }
        }
    }
}
