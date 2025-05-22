package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class winpanel extends JFrame {

    public static boolean winstatus = false;

    public void addjpg() {
        this.setSize(600, 600);
        this.setLocationRelativeTo(null);
        this.setTitle("you are win");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                g.drawImage(images.bg, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(null);


        JButton button = new JButton("返回首页");
        button.setBounds(250, 500, 100, 40);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {//监听button
                int minte=(300-GameBoard.seconds)/60;
                int second=(300-GameBoard.seconds)%60;
                int step=GameBoard.getBoard().getSteps()+1;
                JOptionPane.showMessageDialog(null, "你玩了"+minte+"分钟"+second+"秒,用了 "+step +"步");
                JFrame frame = (JFrame)SwingUtilities.getWindowAncestor((Component)e.getSource());
                frame.dispose();
                SelectLevel.l4.setVisible(false);
                Login.getSelectLevel().setVisible(true);
                BlockButton.i = 0;
                winstatus = true;
            }
        });
        panel.add(button);

        this.setContentPane(panel);
        this.setVisible(true);
    }
}
