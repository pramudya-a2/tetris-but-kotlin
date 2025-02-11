package main;

import mino.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class PlayManager {

    //buat play area nya
    final int WIDTH = 360;
    final int HEIGHT = 600;
    public static int left_x;
    public static int right_x;
    public static int top_y;
    public static int bottom_y;

    //Mino
    Mino currentMino;
    final int MINO_START_X;
    final int MINO_START_Y;
    Mino nextMino;
    final int NEXTMINO_X;
    final int NEXTMINO_Y;
    public static ArrayList<Block> StaticBlocks = new ArrayList<>();

    //other
    public static int dropInterval = 60;
    boolean gameOver;

    //effect
    boolean effectCounterOn;
    int effectCounter;
    ArrayList<Integer> effectY = new ArrayList<>();

    //score
    int level = 1;
    int lines;
    int score;


    public PlayManager() {
        //main frame buat PA
        left_x = (GamePanel.WIDTH/2)- (WIDTH/2); //1200/2 - 360/2 = 460
        right_x = left_x + WIDTH;
        top_y = 50;
        bottom_y = top_y +HEIGHT;

        MINO_START_X = left_x + (WIDTH/2) - Block.SIZE;
        MINO_START_Y = top_y + Block.SIZE;

        NEXTMINO_X = right_x + 175;
        NEXTMINO_Y = top_y + 500;

        //set current Mino
        currentMino = pickMino();
        currentMino.setXY(MINO_START_X, MINO_START_Y);
        nextMino = pickMino();
        nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);
    }
    private Mino pickMino() {

        Mino mino = null;
        int i = new Random().nextInt(7);
        // ini biar mino nya di pick kinda random
        switch (i) {
            case 0: mino = new Mino_L1();break;
            case 1: mino = new Mino_L2();break;
            case 2: mino = new Mino_Square();break;
            case 3: mino = new Mino_Bar();break;
            case 4: mino = new Mino_T();break;
            case 5: mino = new Mino_Z1();break;
            case 6: mino = new Mino_Z2();break;
        }
        return mino;
    }
    public void update() {

        //buat ngecek currentmino active ornot
        if(currentMino.active == false) {
            //if mino not active, put it into static block
            StaticBlocks.add(currentMino.b[0]);
            StaticBlocks.add(currentMino.b[1]);
            StaticBlocks.add(currentMino.b[2]);
            StaticBlocks.add(currentMino.b[3]);

            // check if game over
            if(currentMino.b[0].x == MINO_START_X && currentMino.b[0].y == MINO_START_Y) {
                //this mean currentmino immedeatly collided a vlock and coulnt move atall
                // so it's XY are the sam lvl as nextMino
                gameOver = true;
                GamePanel.music.stop();
                GamePanel.se.play(2, false);
            }

            currentMino.deactivating = false;
            //
            currentMino = nextMino;
            currentMino.setXY(MINO_START_X, MINO_START_Y);
            nextMino = pickMino();
            nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);

            checkDelete();
        }
        else {
            currentMino.update();
        }

    }
    private void checkDelete() {
        //12 bloc delete
        int x = left_x;
        int y = top_y;
        int blockCount = 0;
        int lineCount = 0;

        while(x < right_x && y < bottom_y) {

            for(int i = 0; i < StaticBlocks.size(); i ++)
                if(StaticBlocks.get(i).x == x && StaticBlocks.get(i).y == y ) {
                    //increase count if there is static block
                    blockCount++;

                }
            x += Block.SIZE;

            if(x == right_x) {
                if(blockCount == 12) {

                    effectCounterOn = true;
                    effectY.add(y);

                    for(int i = StaticBlocks.size()-1; i > -1; i--) {
                      //remove block in current yline
                        if(StaticBlocks.get(i).y == y) {
                            StaticBlocks.remove(i);
                        }
                    }

                    lineCount++;
                    lines++;
                    // drop speed
                    // if certain number hit +1 speed
                    //evry 10 line speed +
                    if(lines % 10 == 10 && dropInterval > 1) {

                        level++;
                        if(dropInterval > 10) {
                            dropInterval -= 10;
                        }
                        else {
                            dropInterval -=1;
                        }
                    }


                    //a line has been deleted
                    for(int i = 0; i < StaticBlocks.size(); i++) {
                        //if a block bove = current y > move down
                        if (StaticBlocks.get(i).y < y) {
                            StaticBlocks.get(i).y += Block.SIZE;
                        }
                    }
                }

                blockCount = 0;
                x = left_x;
                y += Block.SIZE;
            }
        }
        //add score
        if(lineCount >0) {
            GamePanel.se.play(1, false);
            int singleLineScore= 10 * level;
            score += singleLineScore * lineCount;
        }
    }
    public void draw(Graphics2D g2) {
        //play Area
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(4f));
        // ini buat boder frame nya
        g2.drawRect(left_x-4, top_y-4, WIDTH+8, HEIGHT+8);

        //mino waiting room
        int x = right_x + 100;
        int y = bottom_y - 200;
        g2.drawRect(x, y, 200, 200);
        g2.setFont(new Font("Arial", Font.BOLD, 30));
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.drawString("UP COMING", x+15, y+30 );

        //score board
        g2.drawRect(x, top_y, 250, 300);
        x += 40;
        y = top_y + 90;
        g2.drawString( "LEVEL: " + level, x, y); y +=70;
        g2.drawString("LINES: " + lines, x, y); y+= 70;
        g2.drawString("SCORE: " + score, x, y);


        //draw currentmino
        if(currentMino != null) {
            currentMino.draw(g2);
        }
        //draw next mino
        nextMino.draw(g2);

        //static block
        for(int i =0; i < StaticBlocks.size(); i++) {
            StaticBlocks.get(i).draw(g2);
        }
        //draw effect
        if(effectCounterOn) {
            effectCounter++;
            g2.setColor(Color.white);
            for(int i = 0; i < effectY.size();i++) {
                g2.fillRect(left_x, effectY.get(i),WIDTH, Block.SIZE);
            }

            if(effectCounter == 10) {
                effectCounterOn = false;
                effectCounter = 0;
                effectY.clear();
            }
        }
        //pause & Game over announce
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(55f));
        if(gameOver){
            x = left_x + 16;
            y = top_y + 320;
            g2.drawString("GAME OVER", x, y);
        }
        else if(KeyHandler.pausePressed) {
            x = left_x + 62;
            y = top_y + 320;
            g2.drawString("PAUSED", x, y);
        }

        //Game Title
        x = 60;
        y = top_y + 320;
        g2.setColor(Color.white);
        g2.setFont(new Font("Arial", Font.BOLD, 60));
        g2.drawString("TETRIS", x+20, y);
    }

}
