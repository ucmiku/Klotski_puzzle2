package music;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class music {
    public static String DESKTOP_PATH = System.getProperty("user.home") + "\\Desktop\\Klotski puzzle\\";
    public static String location=DESKTOP_PATH+"jpop.wav";
    public static String location1=DESKTOP_PATH+"11.wav";
    public static String location2=DESKTOP_PATH+"12.wav";
    public static String location3=DESKTOP_PATH+"13.wav";

    public static void playmusic(){
        try{
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
    public static void playsound(){
        try{
            File musicFile = new File(location1);
            if (musicFile.exists()) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
            } else {
                System.err.println("错误：音乐文件未找到！");
            }
        } catch (Exception e) {
            System.out.println("failed");
        }
    }

    public static void winmusic(){
        try{
            File musicFile = new File(location2);
            if (musicFile.exists()) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
            } else {
                System.err.println("错误：音乐文件未找到！");
            }
        } catch (Exception e) {
            System.out.println("failed");
        }
    }

    public static void losemusic(){
        try{
            File musicFile = new File(location3);
            if (musicFile.exists()) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
            } else {
                System.err.println("错误：音乐文件未找到！");
            }
        } catch (Exception e) {
            System.out.println("failed");
        }
    }
}