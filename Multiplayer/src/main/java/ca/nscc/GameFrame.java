package ca.nscc;

/**
 * Author: W0490409
 * Date: 2024-12-06
 * Filename: GameFrame.java
 * Description: This class is used to set a window for the game to be shown in.
 */

import javax.swing.*;

public class GameFrame extends JFrame {
    GameFrame() {

        this.add(new GamePanel());
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
