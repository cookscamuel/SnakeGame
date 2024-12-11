package ca.nscc;

/**
 * Author: W0490409
 * Date: 2024-12-06
 * Filename: PoisonApple.java
 * Description: Class for creating and displaying poison apple objects.
 */

import java.awt.*;

import static ca.nscc.GamePanel.*;

public class PoisonApple extends GameObjects {

    // Coordinates
    static int poisonX;
    static int poisonY;

    // Create random coordinates within the play area.
    @Override
    public void createNew() {
        poisonX = random.nextInt(((SCREEN_WIDTH - UNIT_SIZE * 2)/UNIT_SIZE))*UNIT_SIZE;
        poisonY = random.nextInt((SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;

    }

    // Detect collision with the player.
    @Override
    public void detectCollision(Player snake) {
        if (totalApples >= 5) {
            if ((snake.getHeadX() == poisonX) && (snake.getHeadY() == poisonY)) {
                running = 4;
            }
        }
    }

    // Draw the apple on screen.
    @Override
    public void drawObject(Graphics g) {
        if (totalApples >= 5) {
            g.setColor(new Color(115, 0, 0));
            g.fillOval(poisonX, poisonY, UNIT_SIZE, UNIT_SIZE);
        }
    }

}