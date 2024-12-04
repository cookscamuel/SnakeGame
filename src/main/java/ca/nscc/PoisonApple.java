package ca.nscc;

import java.awt.*;

import static ca.nscc.GamePanel.*;

public class PoisonApple extends GameObjects {

    static int poisonX;
    static int poisonY;

    @Override
    public void createNew() {
        poisonX = random.nextInt(((SCREEN_WIDTH - UNIT_SIZE * 2)/UNIT_SIZE))*UNIT_SIZE;
        poisonY = random.nextInt((SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;

    }

    @Override
    public void detectCollision() {
        if (applesEaten >= 5) {
            if ((x[0] == poisonX) && (y[0] == poisonY)) {
                running = 4;
            }
        }
    }

    @Override
    public void drawObject(Graphics g) {
        if (applesEaten >= 5) {
            g.setColor(new Color(168, 0, 0));
            g.fillOval(poisonX, poisonY, UNIT_SIZE, UNIT_SIZE);
        }
    }

}