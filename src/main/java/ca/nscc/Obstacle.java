package ca.nscc;

import java.awt.*;

public class Obstacle extends GamePanel {

    static int obX;
    static int obY;


    static void createNew() {
        for (int i = 0; i < 3; i++) {
            obX = random.nextInt((SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
            obY = random.nextInt((SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
        }
    }

    static void detectCollision() {
        for (int i = 0; i < applesEaten * 2; i++) {
            if (((x[0] == Obstacle.obX) && (y[0] == Obstacle.obY)) || ((x[0] == Obstacle.obX + UNIT_SIZE) && (y[0] == Obstacle.obY + UNIT_SIZE)) || ((x[0] == Obstacle.obX) && (y[0] == Obstacle.obY + UNIT_SIZE)) || ((x[0] == Obstacle.obX + UNIT_SIZE) && (y[0] == Obstacle.obY)))  {
                running = false;
            }
        }
    }

    static void drawObject(Graphics g) {

        for (int i = 0; i < applesEaten; i++) {
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
