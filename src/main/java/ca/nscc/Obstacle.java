package ca.nscc;

import java.awt.*;

import static ca.nscc.GamePanel.*;

public class Obstacle extends GameObjects {

    static int obX;
    static int obY;

    @Override
    public void createNew() {
        for (int i = 0; i < 3; i++) {
            obX = random.nextInt((((SCREEN_WIDTH - UNIT_SIZE * 2) - UNIT_SIZE) / UNIT_SIZE)) * UNIT_SIZE;
            obY = random.nextInt(((SCREEN_HEIGHT - UNIT_SIZE) / UNIT_SIZE)) * UNIT_SIZE;
        }
    }

    @Override
    public void detectCollision() {
        if (applesEaten >= 10) {
            if (((x[0] == Obstacle.obX) && (y[0] == Obstacle.obY)) || ((x[0] == Obstacle.obX + UNIT_SIZE) && (y[0] == Obstacle.obY + UNIT_SIZE)) || ((x[0] == Obstacle.obX) && (y[0] == Obstacle.obY + UNIT_SIZE)) || ((x[0] == Obstacle.obX + UNIT_SIZE) && (y[0] == Obstacle.obY))) {
                running = false;
            }
        }

    }

    @Override
    public void drawObject(Graphics g) {
        if (applesEaten >= 10) {
            g.setColor(Color.WHITE);
            g.fillRect(Obstacle.obX + UNIT_SIZE, Obstacle.obY, UNIT_SIZE, UNIT_SIZE);
            g.setColor(Color.WHITE);
            g.fillRect(Obstacle.obX + UNIT_SIZE, Obstacle.obY + UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
            g.setColor(Color.WHITE);
            g.fillRect(Obstacle.obX, Obstacle.obY + UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
            g.setColor(Color.WHITE);
            g.fillRect(Obstacle.obX, Obstacle.obY, UNIT_SIZE, UNIT_SIZE);
        }
    }

}
