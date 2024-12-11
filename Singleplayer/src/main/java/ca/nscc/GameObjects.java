package ca.nscc;

import java.awt.*;

abstract class GameObjects {

    public abstract void createNew();

    public abstract void detectCollision();

    public abstract void drawObject(Graphics g);
}
