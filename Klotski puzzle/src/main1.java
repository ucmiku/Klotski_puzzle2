import GUI.Login;
import music.music;

import java.io.FileNotFoundException;

public class main1 {
    public static void main(String[] args) throws FileNotFoundException {
        music.playmusic();

        Login log = new Login();
        log.setVisible(true);

    }
}