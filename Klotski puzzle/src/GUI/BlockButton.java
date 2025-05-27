package GUI;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class BlockButton extends JComponent {  //角色方块的几个属性：名字，被选择的状态，长宽
    private final String name;
    private boolean selected;
    private final int width,height;
    private Image image;
    public static int i=0;//i用于存储方块的下标

    public BlockButton(int width, int height, boolean selected, String name) {
        this.width = width;
        this.height = height;
        this.selected = selected;
        this.name = name;
        this.image=images.man[i];
        i++;
        setPreferredSize(new Dimension(width,height));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    protected void paintComponent(Graphics g){  //对于被选中的方块进行重新绘制
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (selected) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            g2d.setColor(Color.GRAY);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
        g2d.fillRect(0, 0, width, height);
        g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    }

    //设置选中状态
    public void setSelected(boolean selected){
        this.selected = selected;
        repaint();
    }

    public boolean getSelected(){
        return selected;
    }
}
