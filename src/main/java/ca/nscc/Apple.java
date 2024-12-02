package ca.nscc;

import java.awt.*;

public class Apple extends GamePanel {

    static int appleX;
    static int appleY;

    static void createNew() {
        appleX = random.nextInt((SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }

    static void detectCollision() {
        if ((x[0] == Apple.appleX) && (y[0] == Apple.appleY)) {
            bodyParts++;
            applesEaten++;
            PoisonApple.createNew(); // I change the position of the apple every time an apple is eaten because it was boring to have it in the same spot the whole time.
            Apple.createNew();
            Obstacle.createNew();
            if (((Apple.appleX == Obstacle.obX) && (Apple.appleY == Obstacle.obY)) || ((Apple.appleX == Obstacle.obX + UNIT_SIZE) && (Apple.appleY == Obstacle.obY + UNIT_SIZE)) || ((Apple.appleX == Obstacle.obX) && (Apple.appleY == Obstacle.obY + UNIT_SIZE)) || ((Apple.appleX == Obstacle.obX + UNIT_SIZE) && (Apple.appleY == Obstacle.obY))) {
                Apple.createNew();
            }
        }
    }

    static void drawObject(Graphics g) {
        g.setColor(Color.red);
        g.fillOval(Apple.appleX, Apple.appleY, UNIT_SIZE, UNIT_SIZE);
    }

}
