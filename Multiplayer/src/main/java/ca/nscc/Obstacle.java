package ca.nscc;

/**
 * Author: W0490409
 * Date: 2024-12-06
 * Filename: Obstacle.java
 * Description: Derived class of GameObjects that creates the wall obstacles.
 */

import java.awt.*;

import static ca.nscc.GamePanel.*;

public class Obstacle extends GameObjects {

    // Wall X and Y
    static int obX;
    static int obY;

    // Generate random X and Y somewhere within the play area, but slightly farther away from the right side (the wall is 2x2)
    @Override
    public void createNew() {
        for (int i = 0; i < 3; i++) {
            obX = random.nextInt((((SCREEN_WIDTH - UNIT_SIZE * 2) - UNIT_SIZE) / UNIT_SIZE)) * UNIT_SIZE;
            obY = random.nextInt(((SCREEN_HEIGHT - UNIT_SIZE) / UNIT_SIZE)) * UNIT_SIZE;
        }
    }

    // Check to see if the player hit any of the 4 wall pieces. The coordinates are relational to the one corner of the obstacle, the "true" wall.
    @Override
    public void detectCollision(Player snake) {
        if (totalApples >= 10) {
            if ((snake.getHeadX() == obX) && (snake.getHeadY() == obY)
                    || ((snake.getHeadX() == obX + UNIT_SIZE) && (snake.getHeadY() == obY + UNIT_SIZE))
                    || ((snake.getHeadX() == obX) && (snake.getHeadY() == obY + UNIT_SIZE))
                    || ((snake.getHeadX() == obX + UNIT_SIZE) && (snake.getHeadY() == obY))) {
                running = 4; // End the game if a hit is detected.
            }
        }

    }

    // Draw all four wall pieces.
    @Override
    public void drawObject(Graphics g) {
        if (totalApples >= 10) {
            g.setColor(Color.WHITE);
            g.fillRect(obX + UNIT_SIZE, obY, UNIT_SIZE, UNIT_SIZE);
            g.setColor(Color.WHITE);
            g.fillRect(obX + UNIT_SIZE, obY + UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
            g.setColor(Color.WHITE);
            g.fillRect(obX, obY + UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
            g.setColor(Color.WHITE);
            g.fillRect(obX, obY, UNIT_SIZE, UNIT_SIZE);
        }
    }

}
