package ca.nscc;

/**
 * Author: W0490409
 * Date: 2024-12-06
 * Filename: Apple.java
 * Description: The class used to handle apple placement, collision, and screen-shrinking.
 */

import java.awt.*;

import static ca.nscc.GamePanel.*;

public class Apple extends GameObjects {

    // X and Y positions of the apple.
    int appleX;
    int appleY;

    // When called, this method will generate new random X and Y positions for the apple within the play area.
    @Override
    public void createNew() {
        appleX = random.nextInt(((SCREEN_WIDTH - UNIT_SIZE * 3)/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }
    // I make sure the apple can't spawn somewhere that will be immediately outside the play area upon eating it.


    // The method is called every game tick, it checks to see if the apple was collected by the player primarily.
    @Override
    public void detectCollision(Player snake) {
        if ((snake.getHeadX() == appleX) && (snake.getHeadY() == appleY)) { // If an apple is collected.
            snake.bodyParts++;
            totalApples++;
            createNew();

            // Create new obstacle and poison apple every time an apple is collected.
            Obstacle obstacle = new Obstacle();
            PoisonApple poisonApple = new PoisonApple();

            obstacle.createNew();
            poisonApple.createNew();

            // Check to see if the poison apple was placed on top of the obstacle.
            if (((poisonApple.poisonX == obstacle.obX) && (poisonApple.poisonY == obstacle.obY))
                    || ((poisonApple.poisonX == obstacle.obX + UNIT_SIZE) && (poisonApple.poisonY == obstacle.obY + UNIT_SIZE))
                    || ((poisonApple.poisonX == obstacle.obX) && (poisonApple.poisonY == obstacle.obY + UNIT_SIZE))
                    || ((poisonApple.poisonX == obstacle.obX + UNIT_SIZE) && (poisonApple.poisonY == obstacle.obY))) {
                poisonApple.createNew(); // Move the poison apple if it overlaps with any part of an obstacle.
            }

            // Check to see if an apple spawns on a poison apple and respawn it.
            if ((appleX == poisonApple.poisonX) && (appleY == poisonApple.poisonY)) {
                createNew(); // Generate new coordinates for the apple.
            }

            // Check to see if an apple spawns on any part of an obstacle and respawn it.
            if (((appleX == obstacle.obX) && (appleY == obstacle.obY))
                    || ((appleX == obstacle.obX + UNIT_SIZE) && (appleY == obstacle.obY + UNIT_SIZE))
                    || ((appleX == obstacle.obX) && (appleY == obstacle.obY + UNIT_SIZE))
                    || ((appleX == obstacle.obX + UNIT_SIZE) && (appleY == obstacle.obY))) {
                createNew(); // Generate new coordinates for the apple.
            }

            // I stuck this stuff in here, though it could have been in its own class called "DeathBarrier" or something.
            if (totalApples == 5 || totalApples == 10) { // Increment the count to progress the game for the player.
                level++;
                levelString = String.valueOf(level); // Update the visually displayed score.
            }

            if (totalApples == 15 || totalApples == 20 || totalApples == 25 || totalApples == 30) { // Once the score hits 15, the play area starts shrinking.
                SCREEN_WIDTH -= UNIT_SIZE;
                level++;
                levelString = String.valueOf(level);
            }
            if (totalApples == 35 || totalApples == 40 || totalApples == 45) { // On 35, 40, and 45, the play area shrinks faster.
                SCREEN_WIDTH -= UNIT_SIZE * 2;
                level++;
                levelString = String.valueOf(level);
                if (level == 10) { // After the 45 apple has been eaten, set the level to "Endless." This will go on until the player dies.
                    levelString = "Endless";
                }
            }
        }
    }

    // This is used to draw the apple using the coordinates.
    @Override
    public void drawObject(Graphics g) {
        g.setColor(Color.red);
        g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
    }

}
