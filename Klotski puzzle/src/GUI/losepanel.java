package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class losepanel extends JFrame {

    public void addjpg() {
        this.setSize(600, 600);
        this.setLocationRelativeTo(null);
        this.setTitle("you failed");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                g.drawImage(images.lose, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(null);


        JButton button = new JButton("返回首页");
        button.setBounds(250, 500, 100, 40);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {//监听button
                JOptionPane.showMessageDialog(null, "再接再厉");
                JFrame frame = (JFrame)SwingUtilities.getWindowAncestor((Component)e.getSource());
                frame.dispose();
                SelectLevel.l4.setVisible(false);
                Login.getSelectLevel().setVisible(true);
                BlockButton.i = 0;
            }
        });
        panel.add(button);

        this.setContentPane(panel);
        this.setVisible(true);
    }
}
