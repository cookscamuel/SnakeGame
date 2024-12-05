package ca.nscc;

import java.awt.*;

import static ca.nscc.GamePanel.*;

public class Player {

    private int[] x = new int[GAME_UNITS];
    private int[] y = new int[GAME_UNITS];
    private int applesEaten;

    private int bodyParts;

    private char direction;

    public Player() {
        this.bodyParts = 6;
        this.direction = 'R';
        this.applesEaten = 0;
        for (int i = 1; i < bodyParts; i++) {
            x[i] = -UNIT_SIZE;
        }
        x[0] = SCREEN_WIDTH/2;
        y[0] = 0;
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

    }

    public void drawSnake(Graphics g) {
        for (int i = 0; i < bodyParts; i++) {
            if (i == 0) {
                g.setColor(Color.GREEN);
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            } else {
                g.setColor(new Color(45, 180, 0));
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }
        }
    }
    public void setDirection(char direction) {
        this.direction = direction;
    }
    public int getHeadX() {
        return x[0];
    }

    public int getHeadY() {
        return y[0];
    }

}
