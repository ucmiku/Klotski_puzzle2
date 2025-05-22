package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;

public class tool extends JComponent {
    private final String name;
    private boolean selected;
    private boolean used;
    private final int width,height;
    private Image image;
    public static int i=0;

    public tool(int width, int height, String name) {  //使用used来标记是否被使用
        this.width = width;
        this.height = height;
        this.used = false;
        this.name = name;
        this.image = images.tool[i];
        i++;
        setPreferredSize(new Dimension(width,height));
    }

    protected void paintComponent(Graphics g){  //道具被使用之后的重绘制
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if(used) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            repaint();
        }

        g2d.fillRect(0, 0, width, height);
        g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    }


    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
