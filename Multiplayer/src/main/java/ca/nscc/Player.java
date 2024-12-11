package ca.nscc;

/**
 * Author: W0490409
 * Date: 2024-12-06
 * Filename: Player.java
 * Description: Class used to hold all information about snakes.
 */

import java.awt.*;

import static ca.nscc.GamePanel.*;

public class Player {

    // body coordinates.
    private int[] x = new int[GAME_UNITS];
    private int[] y = new int[GAME_UNITS];

    // Number of body parts for each snake.
    public int bodyParts;

    // Direction each snake is headed.
    private char direction;

    // Used to determine the color of the snake, nothing more.
    int player;

    // Color of the head.
    Color head;

    // Color of the body.
    Color body;

    // Constructor, takes an integer to determine snake color.
    public Player(int player) {
        this.player = player;
        this.bodyParts = 6; // Initialize snake stats.
        this.direction = 'D';
        // Spawn snake body parts outside the screen at first so the player can't see leftover snake pieces.
        for (int i = 1; i < bodyParts; i++) {
            x[i] = -UNIT_SIZE;
        }
        x[0] = SCREEN_WIDTH/2; // Set spawn point for each snake.
        y[0] = 0;
    }

    // Handle movement based on direction. Copied from bro code and barely changed.
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

    // Check for collision with itself or the screen confines.
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

    }

    // Draw the snake using its unique colors.
    public void drawSnake(Graphics g) {
        for (int i = 0; i < bodyParts; i++) {
            if (player == 1) { // This is the check for what color to make the snake.
                head = Color.GREEN;
                body = new Color(45, 180, 0);
            }
            else {
                head = Color.ORANGE;
                body = new Color(255, 129, 0);
            }
            if (i == 0) {
                g.setColor(head);
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            } else {
                g.setColor(body);
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }
        }
    }


    // Setter for direction to allow for movement.
    public void setDirection(char direction) {
        this.direction = direction;
    }

    // Getters for direction and head coordinates for each snake.
    public char getDirection() {
        return direction;
    }

    public int getHeadX() {
        return x[0];
    }

    public int getHeadY() {
        return y[0];
    }

}
