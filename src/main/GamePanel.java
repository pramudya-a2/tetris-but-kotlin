package main;

import javax.swing.*;

import java.awt.*;

import java.awt.event.KeyListener;

public class GamePanel extends JPanel implements Runnable {

    public static final int WIDTH = 1200;
    public static final int HEIGHT = 720;
    final int FPS = 60;
     Thread gameThread;
     PlayManager pm;
     public static Sound music = new Sound();
     public static Sound se = new Sound();
     //se = sound effect


     public GamePanel() {

         //setting di sini
         this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
         this.setBackground(Color.black);
         this.setLayout(null);
         //impelemantasi keylistener
         this.addKeyListener(new KeyHandler());
         this.setFocusable(true);

         pm = new PlayManager();
     }
     public void launch() {
         gameThread = new Thread(this);
         gameThread.start();

         music.play(0, true);
         music.loop();
     }
    @Override
    public void run() {
         // game loop
     double drawinterval = 1000000000/FPS;
     double delta = 0;
     long LastTime = System.nanoTime();
     long currentTime;

     while(gameThread != null) {

         currentTime = System.nanoTime();

         delta += (currentTime - LastTime) / drawinterval;
         LastTime = currentTime;

         // ini buat recal 2 override di bawwah
         if(delta >= 1) {
             update();
             repaint();
             delta --;
         }
     }


    }
    private void update() {
         if(KeyHandler.pausePressed == false && pm.gameOver == false) {
             pm.update();

         }
    }
    public void paintComponent(Graphics g) {
         super.paintComponent(g);

         Graphics2D g2 = (Graphics2D)g;
         pm.draw(g2);
    }
}
