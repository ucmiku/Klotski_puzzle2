package GUI;

import java.awt.*;

public class images {
    public static final String folder_PATH = System.getProperty("user.home") + "\\Desktop\\Klotski puzzle\\";
    public static Image bg = Toolkit.getDefaultToolkit().getImage(folder_PATH+"1.jpg");
    public static Image lose=Toolkit.getDefaultToolkit().getImage(folder_PATH+"9.jpg");
    public static Image backboard=Toolkit.getDefaultToolkit().getImage(folder_PATH+"10.jpg");
    public static Image select=Toolkit.getDefaultToolkit().getImage(folder_PATH+"11.jpg");
    public static Image chessboardImage = Toolkit.getDefaultToolkit().getImage(folder_PATH+"12.jpg");

    public static final Image[] man = {
            Toolkit.getDefaultToolkit().getImage(folder_PATH+"2.jpg"),
            Toolkit.getDefaultToolkit().getImage(folder_PATH+"3.jpg"),
            Toolkit.getDefaultToolkit().getImage(folder_PATH+"4.jpg"),
            Toolkit.getDefaultToolkit().getImage(folder_PATH+"5.jpg"),
            Toolkit.getDefaultToolkit().getImage(folder_PATH+"6.jpg"),
            Toolkit.getDefaultToolkit().getImage(folder_PATH+"7.jpg"),
            Toolkit.getDefaultToolkit().getImage(folder_PATH+"8.jpg"),
            Toolkit.getDefaultToolkit().getImage(folder_PATH+"8.jpg"),
            Toolkit.getDefaultToolkit().getImage(folder_PATH+"8.jpg"),
            Toolkit.getDefaultToolkit().getImage(folder_PATH+"8.jpg")
    };

    public static final Image[] tool = {
            Toolkit.getDefaultToolkit().getImage(folder_PATH+"14.jpg"),
            Toolkit.getDefaultToolkit().getImage(folder_PATH+"13.jpg"),
    };
}
