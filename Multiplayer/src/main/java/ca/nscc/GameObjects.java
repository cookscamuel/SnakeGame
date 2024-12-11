package ca.nscc;

/**
 * Author: W0490409
 * Date: 2024-12-06
 * Filename: GameObjects.java
 * Description: Abstract class that objects like apples, poison apples, and obstacles can extend.
 */

import java.awt.*;

abstract class GameObjects {

    public abstract void createNew();

    public abstract void detectCollision(Player snake);

    public abstract void drawObject(Graphics g);
}
