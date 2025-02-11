package main;

import javax.swing.*;

public class Main {

    public static void main (String[] args) {

        JFrame window = new JFrame("Tetris");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        // buat panel di sini aj
        GamePanel gp = new GamePanel();
        window.add(gp);
        window.pack();
        //pack = size gp >>= size window

        window.setLocationRelativeTo(null);
        window.setVisible(true);
        //buat launcer
        gp.launch();
    }

}
