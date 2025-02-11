package main;

import javax.sound.sampled.*;
import java.net.URL;

public class Sound {
    Clip musicClip;
    URL url[] = new URL[10];

    public Sound() {

        url[0] = getClass().getResource("/aespa-Hold-On-Tight.wav");
        url[1] = getClass().getResource("/delet line.wav");
        url[2] = getClass().getResource("/gameover.wav");
        url[3] = getClass().getResource("/rotation.wav");
        url[4] = getClass().getResource("/touch floor.wav");
    }
    public void play(int i, boolean music) {

        try {
            AudioInputStream aIS = AudioSystem.getAudioInputStream(url[i]);
            Clip clip = AudioSystem.getClip();

            if(music){
                musicClip = clip;
            }

            clip.open(aIS);
            clip.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent event) {
                    if(event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                    }
                }
            });
            aIS.close();
            clip.start();

        }catch(Exception e) {

        }
    }
    public void loop() {
        musicClip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void stop() {
        musicClip.stop();
        musicClip.close();
    }
}
