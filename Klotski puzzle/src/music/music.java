package music;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class music {
    public static void playmusic(){
        try{
            String DESKTOP_PATH = System.getProperty("user.home") + "\\Desktop\\Klotski puzzle\\";
            String location=DESKTOP_PATH+"jpop.wav";
            File musicpath =new File(location);
            if(musicpath.exists()) {
                AudioInputStream  audio = AudioSystem.getAudioInputStream(musicpath);
                Clip clip=AudioSystem.getClip();
                clip.open(audio);
                clip.start();
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }else{
                System.out.println("can't find the path");
            }
        } catch (Exception e) {
            System.out.println("failed");
        }
    }
}