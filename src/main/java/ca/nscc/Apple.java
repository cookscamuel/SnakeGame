package ca.nscc;

import java.awt.*;

import static ca.nscc.GamePanel.*;

public class Apple extends GameObjects {

    static int appleX;
    static int appleY;

    @Override
    public void createNew() {
        appleX = random.nextInt(((SCREEN_WIDTH - UNIT_SIZE * 3)/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }

    @Override
    public void detectCollision() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            createNew();

            PoisonApple poisonApple = new PoisonApple();
            Obstacle obstacle = new Obstacle();
            poisonApple.createNew();
            obstacle.createNew();

            // Check to see if an apple spawns on a poison apple and respawn it.
            if ((appleX == poisonApple.poisonX) && (appleY == poisonApple.poisonY)) {
                createNew();
            }

            // Check to see if an apple spawns on any part of an obstacle and respawn it.
            if (((appleX == obstacle.obX) && (appleY == obstacle.obY))
                    || ((appleX == obstacle.obX + UNIT_SIZE) && (appleY == obstacle.obY + UNIT_SIZE))
                    || ((appleX == obstacle.obX) && (appleY == obstacle.obY + UNIT_SIZE))
                    || ((appleX == obstacle.obX + UNIT_SIZE) && (appleY == obstacle.obY))) {
                createNew();
            }
            if (applesEaten == 5 || applesEaten == 10) {
                level++;
                levelString = String.valueOf(level);
            }

            if (applesEaten == 15 || applesEaten == 20 || applesEaten == 25 || applesEaten == 30) {
                SCREEN_WIDTH -= UNIT_SIZE;
                DRAW_HEIGHT -= UNIT_SIZE;
                level++;
                levelString = String.valueOf(level);
            }
            if (applesEaten == 35 || applesEaten == 40 || applesEaten == 45) {
                SCREEN_WIDTH -= UNIT_SIZE * 2;
                DRAW_HEIGHT -= UNIT_SIZE * 2;
                level++;
                levelString = String.valueOf(level);
            }
            if (level == 10) {
                levelString = "Endless";
            }
        }
    }

    @Override
    public void drawObject(Graphics g) {
        g.setColor(Color.red);
        g.fillOval(Apple.appleX, Apple.appleY, UNIT_SIZE, UNIT_SIZE);
    }

}
