package GUI;

import game_logic.Board;
import music.music;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class winpanel extends JFrame {

    public void addjpg() {
        music.winmusic();

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
                int minte=(GameBoard.seconds)/60;
                int second=(GameBoard.seconds)%60;
                int step=GameBoard.getBoard().getProcess().size()+1;
                JOptionPane.showMessageDialog(null, "你还剩"+minte+"分钟"+second+"秒,用了 "+step +"步");
                BlockButton.i = 0;
                tool.i = 0;
                Login.loginSystem.save(new Board().getcordinate(),new Board().getProcess());
                JFrame frame = (JFrame)SwingUtilities.getWindowAncestor((Component)e.getSource());
                frame.dispose();
                SelectLevel.l4.setVisible(false);
                Login.getSelectLevel().setVisible(true);
            }
        });
        panel.add(button);

        this.setContentPane(panel);
        this.setVisible(true);
    }
}
