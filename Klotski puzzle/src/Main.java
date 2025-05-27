import GUI.Login;
import music.music;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args){
        music.playmusic();

        Login log = new Login();
        log.setVisible(true);
    }
}