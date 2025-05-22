package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClosingFrame extends JFrame{
    JButton Yes = new JButton("是");
    JButton No = new JButton("否");
    JLabel tip = new JLabel("是否保存数据？");

    public ClosingFrame(){
        setSize(240,150);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        tip.setBounds(40,10,160,40);
        Yes.setBounds(40,60,60,30);
        No.setBounds(120,60,60,30);
        add(tip);
        add(Yes);
        add(No);


    }

}
