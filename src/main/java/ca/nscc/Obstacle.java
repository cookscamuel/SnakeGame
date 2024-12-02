package ca.nscc;

public class Obstacle extends GamePanel {

    static int obX;
    static int obY;

    static void createNew() {
        obX = random.nextInt((SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        obY = random.nextInt((SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }

    static void detectCollision() {
        if ((x[0] == Obstacle.obX) && (y[0] == Obstacle.obY)) {
            running = false;
        }
    }

}
