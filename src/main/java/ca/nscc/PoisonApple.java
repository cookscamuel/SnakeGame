package ca.nscc;

import java.awt.*;

public class PoisonApple extends GamePanel {

    static int poisonX;
    static int poisonY;

    static void createNew() {
        poisonX = random.nextInt((SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        poisonY = random.nextInt((SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
        if (poisonX == Apple.appleX && poisonY == Apple.appleY) {
            poisonX += UNIT_SIZE;
            poisonY += UNIT_SIZE;
        }
    }

    static void detectCollision() {
        if ((x[0] == PoisonApple.poisonX) && (y[0] == PoisonApple.poisonY)) {
            running = false;
        }
    }

    static void drawObject(Graphics g) {
        g.setColor(Color.MAGENTA);
        g.fillOval(PoisonApple.poisonX, PoisonApple.poisonY, UNIT_SIZE, UNIT_SIZE);
    }

}